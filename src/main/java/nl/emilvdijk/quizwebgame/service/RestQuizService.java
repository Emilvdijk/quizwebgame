package nl.emilvdijk.quizwebgame.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.stereotype.Service;

/**
 * quiz service for all operations involving quiz questions for the REST api functions on the
 * program.
 *
 * @author Emil van Dijk
 */
@Service
@AllArgsConstructor
public class RestQuizService {

  @NonNull QuestionRepo questionRepo;

  /**
   * find and return all questions from the repository.
   *
   * @return all questions from the repository.
   */
  public List<Question> findAll() {
    return questionRepo.findAll();
  }

  /**
   * find question from the repository by id or else throw a QuestionNotFoundException.
   *
   * @exception QuestionNotFoundException is thrown when a question with the given id can not be
   *     found
   * @param id id to look for
   * @return the question with the corresponding id
   */
  public Question findById(Long id) throws QuestionNotFoundException {
    return questionRepo.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
  }
}
