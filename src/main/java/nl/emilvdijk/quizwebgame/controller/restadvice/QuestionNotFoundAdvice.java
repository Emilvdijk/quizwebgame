package nl.emilvdijk.quizwebgame.controller.restadvice;

import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QuestionNotFoundAdvice {

  @ExceptionHandler(QuestionNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String questionNotFoundHandler(QuestionNotFoundException ex) {
    return ex.getMessage();
  }
}
