package nl.emilvdijk.quizwebgame.service;

import java.util.List;

import lombok.Setter;
import nl.emilvdijk.quizwebgame.api.QuestionsApi;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  @Autowired QuestionRepo questionRepo;
  @Setter Question question = null;

  /**
   * returns question held by quiz service
   * @return question held by quiz service
   */
  public Question getQuestion() {
    if (this.question != null) {
      return this.question;
    } else {
      getnewQuestion();

      return this.question;
    }
  }

  /**
   * gets a new question from the repo if it has questions and sets it to quiz service question
   * if the repo is empty a call will be made to refill the repo
   */
  private void getnewQuestion() {
    if (questionRepo.count() < 1) {
      getNewQuestions();
    }
    List<Question> questions = questionRepo.findAll();
    this.question = questions.getFirst();
    questionRepo.delete(questions.getFirst());
    this.question.prepareAnswers();
  }

  /**
   * gets new questions from the question api and saves them to the repo
   */
  private void getNewQuestions() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    questionRepo.saveAll(newQuestions);
  }
}
