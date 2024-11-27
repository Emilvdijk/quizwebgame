package nl.emilvdijk.quizwebgame.config;

import static org.junit.jupiter.api.Assertions.*;

import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = QuizWebGameApplication.class)
class WebMvcConfigTest {

  @Autowired PasswordEncoder passwordEncoder;

  @Test
  void passwordEncoder() {
    String result = passwordEncoder.encode("myPassword");
    assertTrue(passwordEncoder.matches("myPassword", result));
  }

  @Test
  void mvcSecurityTest() {}
}
// FIXME add more tests with:
//  https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/authentication.html
