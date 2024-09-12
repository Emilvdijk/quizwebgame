package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import nl.emilvdijk.quizwebgame.api.QuestionsApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  @Autowired QuestionRepo questionRepo;
  @Getter List<Question> questions = new ArrayList<>();

  /**
   * returns question held by quiz service
   *
   * @return question held by quiz service
   */
  public Question getQuestion() {
    // FIXME needs to clean its list after login to prevent logged in user to get questions that are
    // already answered
    if (this.questions.isEmpty()) {
      getnewQuestions();
    }
    return this.questions.getFirst();
  }

  private void getnewQuestions() {
    // FIXME change behavior to check if user already answered question
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      MyUser myUser = (MyUser) authentication.getPrincipal();

      List<Question> questionList = questionRepo.findByUserNotContaining(myUser);
      if (questionList.size() < 1) {
        addNewQuestoinsFromApi();
        questionList = questionRepo.findByUserNotContaining(myUser);
      }
      questions = questionList;

    } else {
      questions = questionRepo.findAll();
    }

    this.questions.forEach(Question::prepareAnswers);
  }

  /** gets new questions from the question api and saves them to the repo */
  private void addNewQuestoinsFromApi() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    questionRepo.saveAll(newQuestions);
  }
}
