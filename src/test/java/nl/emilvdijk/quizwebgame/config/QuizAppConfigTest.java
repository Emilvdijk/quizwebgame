package nl.emilvdijk.quizwebgame.config;

import static org.junit.jupiter.api.Assertions.*;

import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuizAppConfigTest {

  @Autowired UserRepo userRepo;

  @Test
  void myUserService() {
    assertEquals(2, userRepo.findAll().size());
  }
}
