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
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizServiceAuthenticated implements QuizService {

  private static final String APPLICABLE_ROLE = "ROLE_USER";
  QuestionRepo questionRepo;
  MyUserService userService;
  QuestionApiService questionApiService;

  /**
   * returns question held by quiz service.
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

  /** FIXME */
  @Override
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
   * FIXME
   *
   * @param userPreferences
   * @param questionIdList
   * @return
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
    // FIXME make it batch insert
    // https://medium.com/@ervrajdesai999/exploring-saveall-method-of-spring-data-jpa-with-batching-properties-of-hibernate-94128d85dff3
    questionRepo.saveAll(newQuestions);
  }

  @Override
  public void removeAnsweredQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    user.getQuestions().removeFirst();
  }

  @Override
  public Question getQuestionByMyid(Long myid) {
    return questionRepo.findById(myid).orElseThrow();
  }

  @Override
  public String getApplicableRole() {
    return APPLICABLE_ROLE;
  }
}
