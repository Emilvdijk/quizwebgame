package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.model.NewMyUser;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class MyUserServiceTest {

  @Autowired MyUserService myUserService;
  @Autowired PasswordEncoder passwordEncoder;

  @Test
  @Order(1)
  void testSaveUser() {
    ArrayList<String> roles = new ArrayList<>();
    roles.add("TEST_ROLE");
    MyUser testSaveUser =
        MyUser.builder()
            .username("testUser")
            .password(passwordEncoder.encode("testPassword"))
            .userPreferences(
                UserPreferences.builder().apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI).build())
            .myRoles(roles)
            .enabled(true)
            .answeredQuestions(new ArrayList<>())
            .build();
    myUserService.saveUser(testSaveUser);
    assertEquals(3, myUserService.userRepo.count());
  }

  @Test
  @Order(2)
  void testLoadUserByUsername() {
    MyUser testloadUser = myUserService.loadUserByUsername("testUser");
    System.out.println(testloadUser.toString());
    assertEquals("testUser", testloadUser.getUsername());
    assertTrue(testloadUser.isEnabled());
    assertTrue(passwordEncoder.matches("testPassword", testloadUser.getPassword()));
    assertEquals("[TEST_ROLE]", testloadUser.getAuthorities().toString());
  }

  @Test
  @Order(3)
  void updateUser() {
    MyUser testIUpdateUser = myUserService.loadUserByUsername("testUser");
    testIUpdateUser.getUserPreferences().setApiChoiceEnum(ApiChoiceEnum.OPENTDB);
    myUserService.updateUser(testIUpdateUser);
    MyUser testUpdatedUser = myUserService.loadUserByUsername("testUser");
    assertEquals(ApiChoiceEnum.OPENTDB, testUpdatedUser.getUserPreferences().getApiChoiceEnum());
  }

  @Test
  @Order(4)
  void registerNewUser() {
    NewMyUser newMyUserTest =
        NewMyUser.builder().username("testUsername").password("testPassword").build();
    myUserService.registerNewUser(newMyUserTest);
    assertEquals("testUsername", myUserService.loadUserByUsername("testUsername").getUsername());
  }

  @Test
  @Order(5)
  void markQuestionDone() {
    MyUser testUser = myUserService.loadUserByUsername("testUser");
    assertEquals(0, testUser.getAnsweredQuestions().size());
    myUserService.markQuestionDone(
        Question.builder().id(2L).build(), testUser, "questionChosenAnswer");
    testUser = myUserService.loadUserByUsername("testUser");
    assertEquals(1, testUser.getAnsweredQuestions().size());
  }

  @Test
  @Order(6)
  void checkIfUserExists() {
    assertFalse(myUserService.checkIfUserExists("asfasdfad"));
    assertTrue(myUserService.checkIfUserExists("testUser"));
  }
}
