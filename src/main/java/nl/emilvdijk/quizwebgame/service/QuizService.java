package nl.emilvdijk.quizwebgame.service;

import java.util.List;
import nl.emilvdijk.quizwebgame.api.QuestionsApi;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  @Autowired
  QuestionRepo questionRepo;

  public List<Question> getQuestions() {
    if (questionRepo.count() < 1) {
      getNewQuestions();
    }
    return questionRepo.findAll();
  }

  public Question getQuestion() {
    List<Question> questions = questionRepo.findAll();
    Question question = questions.getFirst();
    questionRepo.delete(questions.getFirst());
    return question;
  }

  private void getNewQuestions() {
    List<Question> newQuestions = QuestionsApi.getNewQuestion();
    questionRepo.saveAll(newQuestions);
  }
}
