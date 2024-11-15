package nl.emilvdijk.quizwebgame.service;

import static nl.emilvdijk.quizwebgame.entity.Question.difficultyEquals;
import static nl.emilvdijk.quizwebgame.entity.Question.idNotIn;
import static nl.emilvdijk.quizwebgame.entity.Question.isOfCategory;
import static nl.emilvdijk.quizwebgame.entity.Question.originEquals;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nl.emilvdijk.quizwebgame.entity.AnsweredQuestion;
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

  QuestionRepo questionRepo;
  MyUserService userService;
  QuestionApiService questionApiService;
  private static final String APPLICABLE_ROLE = "ROLE_USER";

  /**
   * returns question held by quiz service.
   *
   * @return question held by quiz service
   */
  @Override
  public Question getNewQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // FIXME when a new user is made and logs in the Questions field is null
    //    if (user.getQuestions() == null) {
    //      user.setQuestions(new ArrayList<>());
    //    }

    if (user.getQuestions().isEmpty()) {
      getNewQuestions();
    }
    return user.getQuestions().getFirst();
  }

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

  public Map<AnsweredQuestion, Question> generateAnswersQuestionsMap(MyUser user) {
    Map<AnsweredQuestion, Question> questionsMap = new HashMap<>();
    MyUser myUser = userService.loadUserByUsername(user.getUsername());
    myUser
        .getAnsweredQuestions()
        .forEach(
            answeredQuestion ->
                questionsMap.put(
                    answeredQuestion, getQuestionByMyid(answeredQuestion.getQuestionId())));

    // sorts the map based on the date of the answeredQuestion.
    return questionsMap.entrySet().stream()
        .sorted(Comparator.comparing(value -> value.getKey().getAdded(), Comparator.reverseOrder()))
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new));
  }
}
