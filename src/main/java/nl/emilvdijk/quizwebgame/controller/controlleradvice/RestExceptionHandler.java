package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(QuestionNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String questionNotFoundHandler(QuestionNotFoundException ex) {
    return ex.getMessage();
  }
}
