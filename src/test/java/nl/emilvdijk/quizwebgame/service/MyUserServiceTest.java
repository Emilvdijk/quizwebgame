package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.model.NewMyUser;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext
class MyUserServiceTest {

  @Autowired MyUserService myUserService;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired QuestionRepo questionRepo;

  @BeforeAll
  static void addTestUsers(
      @Autowired PasswordEncoder passwordEncoder, @Autowired MyUserService myUserService) {
    ArrayList<String> roles = new ArrayList<>();
    roles.add("TEST_ROLE");
    MyUser testSaveUser =
        MyUser.builder()
            .username("testUser")
            .password(passwordEncoder.encode("testPassword"))
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .build())
            .myRoles(roles)
            .enabled(true)
            .answeredQuestions(Set.of())
            .build();
    myUserService.saveUser(testSaveUser);
    assertNotNull(myUserService.loadUserByUsername("testUser"));

    MyUser testSaveUser2 =
        MyUser.builder()
            .username("testUser2")
            .password("test")
            .myRoles(List.of("TEST_ROLE"))
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPEN_TDB)
                    .difficultyEnum(DifficultyEnum.MEDIUM)
                    .categoryOpenTdbList(
                        List.of(CategoryOpenTdb.HISTORY, CategoryOpenTdb.GENERAL_KNOWLEDGE))
                    .categoryTriviaApiList(
                        List.of(CategoryTriviaApi.HISTORY, CategoryTriviaApi.SCIENCE))
                    .build())
            .build();
    myUserService.saveUser(testSaveUser2);
    assertNotNull(myUserService.loadUserByUsername("testUser2"));
  }

  @Test
  @Order(2)
  void testLoadUserByUsername() {
    MyUser testLoadUser = myUserService.loadUserByUsername("testUser");
    System.out.println(testLoadUser.toString());
    assertEquals("testUser", testLoadUser.getUsername());
    assertTrue(testLoadUser.isEnabled());
    assertTrue(passwordEncoder.matches("testPassword", testLoadUser.getPassword()));
    assertEquals("[TEST_ROLE]", testLoadUser.getAuthorities().toString());
  }

  @Test
  @Order(3)
  void updateUser() {
    MyUser testIUpdateUser = myUserService.loadUserByUsername("testUser");
    testIUpdateUser.getUserPreferences().setApiChoiceEnum(ApiChoiceEnum.OPEN_TDB);
    myUserService.updateUser(testIUpdateUser);
    MyUser testUpdatedUser = myUserService.loadUserByUsername("testUser");
    assertEquals(ApiChoiceEnum.OPEN_TDB, testUpdatedUser.getUserPreferences().getApiChoiceEnum());
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
    questionRepo.save(
        Question.builder()
            .id(2L)
            .questionText("test")
            .category("test")
            .correctAnswer("test")
            .difficulty("test")
            .incorrectAnswers(List.of())
            .build());
    MyUser testUser = myUserService.loadUserByUsername("testUser");
    Question question = questionRepo.findById(1L).orElseThrow();
    assertEquals(0, testUser.getAnsweredQuestions().size());
    myUserService.markQuestionDone(question, testUser, "questionChosenAnswer");
    testUser = myUserService.loadUserByUsername("testUser");
    assertEquals(1, testUser.getAnsweredQuestions().size());
  }

  @Test
  @Order(6)
  void checkIfUserExists() {
    assertFalse(myUserService.checkIfUserExists("asfasdfad"));
    assertTrue(myUserService.checkIfUserExists("testUser"));
  }

  @Test
  void resetUserSettings() {
    MyUser testUser = myUserService.loadUserByUsername("testUser2");

    myUserService.resetUserSettings(testUser);

    assertTrue(testUser.getUserPreferences().getCategoryOpenTdbList().isEmpty());
    assertTrue(testUser.getUserPreferences().getCategoryTriviaApiList().isEmpty());
    assertEquals(DifficultyEnum.ALL, testUser.getUserPreferences().getDifficultyEnum());
    assertEquals(ApiChoiceEnum.ALL, testUser.getUserPreferences().getApiChoiceEnum());

    testUser = myUserService.loadUserByUsername("testUser2");

    assertTrue(testUser.getUserPreferences().getCategoryOpenTdbList().isEmpty());
    assertTrue(testUser.getUserPreferences().getCategoryTriviaApiList().isEmpty());
    assertEquals(DifficultyEnum.ALL, testUser.getUserPreferences().getDifficultyEnum());
    assertEquals(ApiChoiceEnum.ALL, testUser.getUserPreferences().getApiChoiceEnum());
  }
}
