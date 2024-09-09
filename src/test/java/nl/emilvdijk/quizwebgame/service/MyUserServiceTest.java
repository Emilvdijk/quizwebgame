package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.QuizwebgameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = QuizwebgameApplication.class)
@TestMethodOrder(OrderAnnotation.class)
class MyUserServiceTest {

  @Autowired MyUserService myUserService;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
  @Order(1)
  void save() {

    ArrayList<String> roles = new ArrayList<>();
    roles.add("TESTROLE");
    MyUser testsaveuser =
        MyUser.builder()
            .username("testuser")
            .password(passwordEncoder.encode("testpassword"))
            .myRoles(roles)
            .enabled(true)
            .build();
    myUserService.save(testsaveuser);

    //    UserDetails testsaveuserdb = this.myUserService.userRepo.save(testsaveuser);
    //    System.out.println("testsaveuserdb = " + testsaveuserdb);
  }

  @Test
  @Order(2)
  void loadUserByUsername() {
    MyUser testloaduser = myUserService.loadUserByUsername("testuser");
    System.out.println(testloaduser.toString());
    assertEquals(testloaduser.getUsername(), "testuser");
    assertTrue(testloaduser.isEnabled());
    assertTrue(passwordEncoder.matches("testpassword", testloaduser.getPassword()));
    assertEquals("[TESTROLE]", testloaduser.getAuthorities().toString());
  }
}
