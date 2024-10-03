package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceAuthenticated implements QuizService {

  @Autowired QuestionRepo questionRepo;
  @Autowired UserRepo userRepo;
  @Autowired QuestionsApiService questionsApiService;
  private static final String APPLICABLE_ROLE = "ROLE_USER";

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

  @Override
  public void getNewQuestions() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    MyUser myUser = userRepo.findByUsername(user.getUsername());

    List<Question> questions = getQuestionsByChoice(myUser);

    if (questions.size() < 10) {
      addNewQuestionsFromApi();
      questions = getQuestionsByChoice(myUser);
    }
    questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
    user.setQuestions(questions);
  }

  private List<Question> getQuestionsByChoice(MyUser myUser) {
    List<Long> questionIdList = new ArrayList<>();
    myUser.getAnsweredQuestions().forEach(question -> questionIdList.add(question.getMyId()));
    switch (myUser.getApiChoiceEnum()) {
      case TRIVIAAPI:
        return questionRepo.findBymyIdNotInAndOrigin(questionIdList, ApiChoiceEnum.TRIVIAAPI);
      case OPENTDB:
        return questionRepo.findBymyIdNotInAndOrigin(questionIdList, ApiChoiceEnum.OPENTDB);
      default:
        return questionRepo.findBymyIdNotIn(questionIdList);
    }
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    List<Question> newQuestions = questionsApiService.getNewQuestions();
    questionRepo.saveAll(newQuestions);
  }

  @Override
  public void removeAnsweredQuestion() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    user.getQuestions().removeFirst();
  }

  @Override
  public Question getQuestionByMyid(Long myid) {
    return questionRepo.findBymyId(myid);
  }

  @Override
  public String getApplicableRole() {
    return APPLICABLE_ROLE;
  }
}
