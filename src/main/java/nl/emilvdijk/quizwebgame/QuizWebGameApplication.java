package nl.emilvdijk.quizwebgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * base Main class for the program. no special changes made.
 *
 * @author Emil van Dijk
 */
@SpringBootApplication
public class QuizWebGameApplication {

  /**
   * base main method of the program. no special changes made. the arguments are unused.
   *
   * @param args the arguments are unused.
   */
  public static void main(String[] args) {
    SpringApplication.run(QuizWebGameApplication.class, args);
  }
}
