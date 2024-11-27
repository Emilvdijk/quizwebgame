package nl.emilvdijk.quizwebgame.exceptions;

import java.io.Serial;

/**
 * exception thrown when an api call to one of the question suppliers runs into an error. the user
 * preferences will also be reset by the handler when thrown.
 *
 * @see nl.emilvdijk.quizwebgame.controller.controlleradvice.GlobalExceptionHandler
 * @author Emil van Dijk
 */
public class ApiErrorException extends RuntimeException {

  @Serial private static final long serialVersionUID = 2521996807562553728L;

  public ApiErrorException(String message) {
    super(message + " your settings have been reset, sorry for the inconvenience.");
  }
}
