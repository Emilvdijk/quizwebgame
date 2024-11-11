package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.model.MyUserDto;
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
    roles.add("TESTROLE");
    MyUser testSaveUser =
        MyUser.builder()
            .username("testuser")
            .password(passwordEncoder.encode("testpassword"))
            .userPreferences(
                UserPreferences.builder().apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI).build())
            .myRoles(roles)
            .enabled(true)
            .answeredQuestions(new ArrayList<>())
            .build();
    myUserService.saveUser(testSaveUser);
  }

  @Test
  @Order(2)
  void testLoadUserByUsername() {
    MyUser testloadUser = myUserService.loadUserByUsername("testuser");
    System.out.println(testloadUser.toString());
    assertEquals("testuser", testloadUser.getUsername());
    assertTrue(testloadUser.isEnabled());
    assertTrue(passwordEncoder.matches("testpassword", testloadUser.getPassword()));
    assertEquals("[TESTROLE]", testloadUser.getAuthorities().toString());
  }

  @Test
  @Order(3)
  void updateUser() {
    MyUser testIUpdateUser = myUserService.loadUserByUsername("testuser");
    testIUpdateUser.getUserPreferences().setApiChoiceEnum(ApiChoiceEnum.OPENTDB);
    myUserService.updateUser(testIUpdateUser);
    MyUser testUpdatedUser = myUserService.loadUserByUsername("testuser");
    assertEquals(ApiChoiceEnum.OPENTDB, testUpdatedUser.getUserPreferences().getApiChoiceEnum());
  }

  @Test
  @Order(4)
  void registerNewUser() {
    MyUserDto myUserDtoTest =
        MyUserDto.builder().username("testusername").password("testpassword").build();
    myUserService.registerNewUser(myUserDtoTest);
    assertEquals("testusername", myUserService.loadUserByUsername("testusername").getUsername());
  }

  @Test
  @Order(5)
  void markQuestionDone() {
    MyUser testUser = myUserService.loadUserByUsername("testuser");
    assertEquals(0, testUser.getAnsweredQuestions().size());
    myUserService.markQuestionDone(Question.builder().id(2L).build(), testUser);
    testUser = myUserService.loadUserByUsername("testuser");
    assertEquals(1, testUser.getAnsweredQuestions().size());
  }

  @Test
  @Order(6)
  void checkIfUserExists() {
    assertNotEquals(true, myUserService.checkIfUserExists("asfasdfad"));
    assertEquals(true, myUserService.checkIfUserExists("testuser"));
  }
}
