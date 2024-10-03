package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = QuizWebGameApplication.class)
@TestMethodOrder(OrderAnnotation.class)
class MyUserServiceTest {

  @Autowired MyUserService myUserService;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
  @Order(1)
  void saveUser() {

    ArrayList<String> roles = new ArrayList<>();
    roles.add("TESTROLE");
    MyUser testSaveUser =
        MyUser.builder()
            .username("testuser")
            .password(passwordEncoder.encode("testpassword"))
            .myRoles(roles)
            .enabled(true)
            .build();
    myUserService.saveUser(testSaveUser);
  }

  @Test
  @Order(2)
  void loadUserByUsername() {
    MyUser testloadUser = myUserService.loadUserByUsername("testuser");
    System.out.println(testloadUser.toString());
    assertEquals("testuser", testloadUser.getUsername());
    assertTrue(testloadUser.isEnabled());
    assertTrue(passwordEncoder.matches("testpassword", testloadUser.getPassword()));
    assertEquals("[TESTROLE]", testloadUser.getAuthorities().toString());
  }

}
