package nl.emilvdijk.quizwebgame.exceptions;

public class QuestionNotFoundException extends RuntimeException {

  public QuestionNotFoundException(Long id) {
    super("could not find question with id: " + id);
  }
}
