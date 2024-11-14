package nl.emilvdijk.quizwebgame.exceptions;

import java.io.Serial;

public class ApiErrorException extends RuntimeException {

  @Serial private static final long serialVersionUID = 2521996807562553728L;

  public ApiErrorException(String message) {
    super(message + " your settings have been reset, sorry for the inconvenience.");
  }
}
