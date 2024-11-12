package nl.emilvdijk.quizwebgame.exceptions;

public class ApiErrorException extends RuntimeException {

  public ApiErrorException(String message) {
    super(message + " your settings have been reset, sorry for the inconvenience.");
  }
}
