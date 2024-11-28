package nl.emilvdijk.quizwebgame.exceptions;

import java.io.Serial;

/**
 * exception for when a question is not found in the database.
 *
 * @see nl.emilvdijk.quizwebgame.controller.controlleradvice.RestExceptionHandler
 * @author Emil van Dijk
 */
public class QuestionNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -5461378526797788444L;

  /**
   * Constructs a QuestionNotFoundException with the specified message.
   *
   * @param id id of the question not found
   */
  public QuestionNotFoundException(Long id) {
    super("could not find question with id: " + id);
  }
}
