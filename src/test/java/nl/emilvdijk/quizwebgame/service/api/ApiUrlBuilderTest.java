package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class ApiUrlBuilderTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

  @Autowired ApiUrlBuilder apiUrlBuilder;

  @Test
  void generateTriviaApiUri() {
    MyUser user =
        MyUser.builder()
            .username("testUser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.EASY)
                    .categoryOpenTdbList(new ArrayList<>())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50&difficulties=easy",
        apiUrlBuilder.generateTriviaApiUri(user).toString());

    MyUser user2 =
        MyUser.builder()
            .username("testUser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.MEDIUM)
                    .categoryOpenTdbList(new ArrayList<>())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50&difficulties=medium",
        apiUrlBuilder.generateTriviaApiUri(user2).toString());

    MyUser user3 =
        MyUser.builder()
            .username("testUser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTdbList(new ArrayList<>())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50",
        apiUrlBuilder.generateTriviaApiUri(user3).toString());

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50&categories=history,general_knowledge",
        apiUrlBuilder
            .generateTriviaApiUri(
                MyUser.builder()
                    .username("testUser")
                    .password("test")
                    .myRoles(List.of())
                    .userPreferences(
                        UserPreferences.builder()
                            .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                            .difficultyEnum(DifficultyEnum.ALL)
                            .categoryOpenTdbList(new ArrayList<>())
                            .categoryTriviaApiList(
                                List.of(
                                    CategoryTriviaApi.HISTORY, CategoryTriviaApi.GENERAL_KNOWLEDGE))
                            .build())
                    .build())
            .toString());
  }

  @Test
  void generateUriOpenTdb() {
    MyUser user =
        MyUser.builder()
            .username("testUser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPEN_TDB)
                    .difficultyEnum(DifficultyEnum.EASY)
                    .categoryOpenTdbList(new ArrayList<>())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50&difficulty=easy",
        apiUrlBuilder.generateUriOpenTdb(user).toString());

    MyUser user2 =
        MyUser.builder()
            .username("testUser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPEN_TDB)
                    .difficultyEnum(DifficultyEnum.MEDIUM)
                    .categoryOpenTdbList(new ArrayList<>())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50&difficulty=medium",
        apiUrlBuilder.generateUriOpenTdb(user2).toString());

    MyUser user3 =
        MyUser.builder()
            .username("testUser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPEN_TDB)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTdbList(new ArrayList<>())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50",
        apiUrlBuilder.generateUriOpenTdb(user3).toString());
    assertEquals(
        "https://opentdb.com/api.php?amount=50&category=27",
        apiUrlBuilder
            .generateUriOpenTdb(
                MyUser.builder()
                    .username("testUser")
                    .password("test")
                    .myRoles(List.of())
                    .userPreferences(
                        UserPreferences.builder()
                            .apiChoiceEnum(ApiChoiceEnum.OPEN_TDB)
                            .difficultyEnum(DifficultyEnum.ALL)
                            .categoryOpenTdbList(
                                List.of(CategoryOpenTdb.ANIMALS, CategoryOpenTdb.CELEBRITIES))
                            .categoryTriviaApiList(List.of())
                            .build())
                    .build())
            .toString());
  }
}
