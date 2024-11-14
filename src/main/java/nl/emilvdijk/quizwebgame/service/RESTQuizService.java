package nl.emilvdijk.quizwebgame.service;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RESTQuizService {

  QuestionRepo questionRepo;

  public List<Question> findAll() {
    return questionRepo.findAll();
  }

  public Question findById(Long id) {
    return questionRepo.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
  }
}
