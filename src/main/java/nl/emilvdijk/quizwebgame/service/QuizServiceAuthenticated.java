package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
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
  @Getter private final String applicableRole = "ROLE_USER";

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
    if (questionRepo.count() < 10) {
      addNewQuestionsFromApi();
    }
    MyUser myUser = userRepo.findByUsername(user.getUsername());
    List<Long> questionIdList = new ArrayList<>();
    myUser.getAnsweredQuestions().forEach(question -> questionIdList.add(question.getMyId()));
    List<Question> questionList = questionRepo.findByMyidNotIn(questionIdList);
    if (questionList.size() < 10) {
      addNewQuestionsFromApi();
      questionList = questionRepo.findByMyidNotIn(questionIdList);
    }
    questionList.forEach(Question::prepareAnswers);
    Collections.shuffle(questionList);
    user.setQuestions(questionList);
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    List<Question> newQuestions = questionsApiService.getNewQuestion();
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
}
