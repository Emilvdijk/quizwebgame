package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.api.QuestionsApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceAuthenticated implements QuizService{

  @Autowired QuestionRepo questionRepo;

  @Override
  public void getNewQuestions() {
    if (questionRepo.count() < 10) {
      addNewQuestionsFromApi();
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      MyUser myUser = (MyUser) authentication.getPrincipal();
      List<Long> questionIdList = new ArrayList<>();
      myUser.getAnsweredQuestions().forEach(question -> questionIdList.add(question.getMyId()));
      List<Question> questionList = questionRepo.findByMyidNotIn(questionIdList);
      if (questionList.size() < 10) {
        addNewQuestionsFromApi();
        questionList = questionRepo.findByMyidNotIn(questionIdList);
      }
      questions = questionList;

    } else {
      questions = questionRepo.findAll();
    }

    this.questions.forEach(Question::prepareAnswers);
    Collections.shuffle(questions);
  }

  /**
   * returns question held by quiz service.
   *
   * @return question held by quiz service
   */
  @Override
  public Question getNewQuestion() {
    if (this.questions.isEmpty()) {
      getNewQuestions();
    }
    return this.questions.getFirst();
  }

  /** gets new questions from the question api and saves them to the repo. */
  @Override
  public void addNewQuestionsFromApi() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    questionRepo.saveAll(newQuestions);
  }
}
