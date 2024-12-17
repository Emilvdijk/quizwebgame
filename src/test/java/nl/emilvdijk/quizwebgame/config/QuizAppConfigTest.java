package nl.emilvdijk.quizwebgame.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
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
    List<MyUser> userList = userRepo.findAll();
    userList.forEach(
        myUser ->
            assertTrue(
                myUser.getUsername().equals("admin") || myUser.getUsername().equals("user")));
  }
}
