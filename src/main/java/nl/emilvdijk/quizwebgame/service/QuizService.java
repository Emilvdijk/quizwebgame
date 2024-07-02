package nl.emilvdijk.quizwebgame.service;

import nl.emilvdijk.quizwebgame.core.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

  QuestionRepo questionRepo;

  public List<Question> getQuestions() {
    return questionRepo.findAll();
  }
}
