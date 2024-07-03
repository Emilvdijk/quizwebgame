package nl.emilvdijk.quizwebgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class QuizwebgameApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuizwebgameApplication.class, args);
  }
}
