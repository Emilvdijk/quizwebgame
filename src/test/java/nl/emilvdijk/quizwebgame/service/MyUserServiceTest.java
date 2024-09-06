package nl.emilvdijk.quizwebgame.service;

import nl.emilvdijk.quizwebgame.QuizwebgameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = QuizwebgameApplication.class)
class MyUserServiceTest {

  @Autowired MyUserService myUserService;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
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
  void loadUserByUsername() {
    MyUser testloaduser =
            myUserService.loadUserByUsername("testuser");
    System.out.println(testloaduser.toString());
  }
}