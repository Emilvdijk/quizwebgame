package nl.emilvdijk.quizwebgame.service;

import static nl.emilvdijk.quizwebgame.entity.Question.difficultyEquals;
import static nl.emilvdijk.quizwebgame.entity.Question.idNotIn;
import static nl.emilvdijk.quizwebgame.entity.Question.isOfCategory;
import static nl.emilvdijk.quizwebgame.entity.Question.originEquals;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * quiz service for all operations involving quiz questions for authenticated users.
 *
 * @author Emil van Dijk
 */
@Service
@AllArgsConstructor
public class QuizServiceAuthenticated implements QuizService {

  @NonNull private static final String APPLICABLE_ROLE = "ROLE_USER";
  @NonNull QuestionRepo questionRepo;
  @NonNull MyUserService userService;
  @NonNull QuestionApiService questionApiService;

  /**
   * returns question held by the current authenticated user. if no questions are found call for the
   * getNewQuestions will be made for adding new quiz questions.
   *
   * @return question held by quiz service
   */
  @Override
  public Question getNewQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (user.getQuestions().isEmpty()) {
      getNewQuestions();
    }
    return user.getQuestions().getFirst();
  }

  /**
   * attempts to fetch new questions from the repository accounting for the current user preferences
   * and excluding the questions the user has already answered. if fewer than 10 questions are found
   * in the repository new questions will be requested from one of the external apis.
   */
  public void getNewQuestions() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser = userService.loadUserByUsername(user.getUsername());
    List<Long> questionIdList = new ArrayList<>();
    myUser
        .getAnsweredQuestions()
        .forEach(answeredQuestion -> questionIdList.add(answeredQuestion.getId()));
    List<Question> questions = getQuestionsByChoice(myUser.getUserPreferences(), questionIdList);

    if (questions.size() < 10) {
      addNewQuestionsFromApi();
      questions = getQuestionsByChoice(myUser.getUserPreferences(), questionIdList);
    }
    questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
    user.setQuestions(questions);
  }

  /**
   * retrieves questions from the repository taking into account the current user preferences and
   * excluding the questions the user has already answered. spring specifications are used
   *
   * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">spring
   *     documentation about spring specifications</a>
   * @param userPreferences the user preferences of the user
   * @param questionIdList the list of question ids to exclude
   * @return a list of questions
   */
  public List<Question> getQuestionsByChoice(
      UserPreferences userPreferences, List<Long> questionIdList) {
    return questionRepo.findAll(
        where(difficultyEquals(userPreferences.getDifficultyEnum()))
            .and(idNotIn(questionIdList))
            .and(originEquals(userPreferences.getApiChoiceEnum()))
            .and(isOfCategory(userPreferences)));
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser = userService.loadUserByUsername(user.getUsername());
    List<Question> newQuestions = questionApiService.getNewQuestions(myUser);
    questionRepo.saveAll(newQuestions);
  }

  /** remove the question from the list held by the user that the user has just answered. */
  @Override
  public void removeAnsweredQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!user.getQuestions().isEmpty()) {
      user.getQuestions().removeFirst();
    }
  }

  /**
   * returns the question from the repository with the desired id.
   *
   * @param id the id of the desired question
   * @return the question with the corresponding id
   */
  @Override
  public Question getQuestionById(Long id) {
    return questionRepo.findById(id).orElseThrow();
  }

  /**
   * returns the applicable role used by the dynamic quiz service.
   *
   * @see DynamicQuizService
   * @return the role this service is applicable to
   */
  @Override
  public String getApplicableRole() {
    return APPLICABLE_ROLE;
  }
}
