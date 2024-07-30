package nl.emilvdijk.quizwebgame.config;

import nl.emilvdijk.quizwebgame.QuizwebgameApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

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
