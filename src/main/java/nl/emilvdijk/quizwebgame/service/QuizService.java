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
    if(questionRepo.count()<10){
      addNewQuestionsFromApi();
    }
    // FIXME change behavior to check if user already answered question
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      MyUser myUser = (MyUser) authentication.getPrincipal();
      List<Long> questionIdList = new ArrayList<>();
      myUser.getAnsweredQuestions().forEach(question -> questionIdList.add(question.getMyid()));
      List<Question> questionList = questionRepo.findBymyidNotIn(questionIdList);
      if (questionList.size() < 10) {
        addNewQuestionsFromApi();
        questionList = questionRepo.findBymyidNotIn(questionIdList);
      }
      questions = questionList;

    } else {
      questions = questionRepo.findAll();
    }

    this.questions.forEach(Question::prepareAnswers);
  }

  /** gets new questions from the question api and saves them to the repo */
  private void addNewQuestionsFromApi() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    questionRepo.saveAll(newQuestions);
  }
}
