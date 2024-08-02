package nl.emilvdijk.quizwebgame.config;

import static org.junit.jupiter.api.Assertions.*;

import nl.emilvdijk.quizwebgame.QuizwebgameApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = QuizwebgameApplication.class)
class WebMvcConfigTest {

  @Autowired PasswordEncoder passwordEncoder;

  @Test
  void passwordEncoder() {
    String result = passwordEncoder.encode("myPassword");
    //    System.out.println(result);
    assertTrue(passwordEncoder.matches("myPassword", result));
  }
}
