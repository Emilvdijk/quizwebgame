package nl.emilvdijk.quizwebgame.service;

import static nl.emilvdijk.quizwebgame.entity.Question.DifficultyEquals;
import static nl.emilvdijk.quizwebgame.entity.Question.IdNotIn;
import static nl.emilvdijk.quizwebgame.entity.Question.OriginEquals;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceAuthenticated implements QuizService {

  QuestionRepo questionRepo;
  UserRepo userRepo;
  QuestionApiService questionApiService;
  private static final String APPLICABLE_ROLE = "ROLE_USER";

  public QuizServiceAuthenticated(
      @Autowired QuestionRepo questionRepo,
      @Autowired UserRepo userRepo,
      @Autowired QuestionApiService questionApiService) {
    this.questionRepo = questionRepo;
    this.userRepo = userRepo;
    this.questionApiService = questionApiService;
  }

  /**
   * returns question held by quiz service.
   *
   * @return question held by quiz service
   */
  @Override
  public Question getNewQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // FIXME when a new user is made and logs in the Questions field is null
    if (user.getQuestions() == null) {
      user.setQuestions(new ArrayList<>());
    }

    if (user.getQuestions().isEmpty()) {
      getNewQuestions();
    }
    return user.getQuestions().getFirst();
  }

  @Override
  public void getNewQuestions() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser =
        userRepo
            .findByUsername(user.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

    List<Long> questionIdList = new ArrayList<>();
    myUser.getAnsweredQuestions().forEach(question -> questionIdList.add(question.getId()));
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
      // FIXME add category check
      UserPreferences userPreferences, List<Long> questionIdList) {
    return questionRepo.findAll(
        where(DifficultyEquals(userPreferences.getDifficultyEnum()))
            .and(IdNotIn(questionIdList))
            .and(OriginEquals(userPreferences.getApiChoiceEnum())));
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser =
        userRepo
            .findByUsername(user.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
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

  public List<Question> findQuestion(Question probe) {
    return questionRepo.findAll(Example.of(probe));
  }
}
