package nl.emilvdijk.quizwebgame;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * base Main class for the program. no special changes made.
 *
 * @author Emil van Dijk
 */
@SpringBootApplication
@Slf4j
public class QuizWebGameApplication {

  /**
   * base main method of the program. no special changes made. the arguments are unused.
   *
   * @param args the arguments are unused.
   */
  public static void main(String[] args) {
    SpringApplication.run(QuizWebGameApplication.class, args);
    log.info(
        """

            \033[1;34m\
              _    _      _ _         __          __        _     _ _
             | |  | |    | | |        \\ \\        / /       | |   | | |
             | |__| | ___| | | ___     \\ \\  /\\  / /__  _ __| | __| | |
             |  __  |/ _ \\ | |/ _ \\     \\ \\/  \\/ / _ \\| '__| |/ _` | |
             | |  | |  __/ | | (_) |     \\  /\\  / (_) | |  | | (_| |_|
             |_|  |_|\\___|_|_|\\___( )     \\/  \\/ \\___/|_|  |_|\\__,_(_)
                                  |/
            \033[0m""");
  }
}
