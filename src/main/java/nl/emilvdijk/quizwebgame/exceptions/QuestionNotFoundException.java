package nl.emilvdijk.quizwebgame.exceptions;

import java.io.Serial;

public class QuestionNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = -5461378526797788444L;

  public QuestionNotFoundException(Long id) {
    super("could not find question with id: " + id);
  }
}
