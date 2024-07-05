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

  public List<Question> getQuestions() {
    if (questionRepo.count() < 1) {
      getNewQuestions();
    }
    return questionRepo.findAll();
  }

  public Question getQuestion() {
    if (this.question != null) {
      return this.question;
    } else {
      getnewQuestion();

      return this.question;
    }
  }

  private void getnewQuestion() {
    if (questionRepo.count() < 1) {
      getNewQuestions();
    }
    List<Question> questions = questionRepo.findAll();
    this.question = questions.getFirst();
    questionRepo.delete(questions.getFirst());
    this.question.prepareAnswers();
  }

  private void getNewQuestions() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    questionRepo.saveAll(newQuestions);
  }
}
