package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTDB;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
class ApiUrlBuilderTest {

  @Autowired ApiUrlBuilder apiUrlBuilder;

  @Test
  void generateTriviaApiUri() {
    MyUser user =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                    .difficultyEnum(DifficultyEnum.EASY)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50&difficulties=easy",
        apiUrlBuilder.generateTriviaApiUri(user).toString());

    MyUser user2 =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                    .difficultyEnum(DifficultyEnum.MEDIUM)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
                    .categoryTriviaApi(List.of())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50&difficulties=medium",
        apiUrlBuilder.generateTriviaApiUri(user2).toString());

    MyUser user3 =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50",
        apiUrlBuilder.generateTriviaApiUri(user3).toString());
  }

  @Test
  void generateUriOpenTdb() {
    MyUser user =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                    .difficultyEnum(DifficultyEnum.EASY)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50&difficulty=easy",
        apiUrlBuilder.generateUriOpenTdb(user).toString());

    MyUser user2 =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                    .difficultyEnum(DifficultyEnum.MEDIUM)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50&difficulty=medium",
        apiUrlBuilder.generateUriOpenTdb(user2).toString());

    MyUser user3 =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
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
                    .userPreferences(
                        UserPreferences.builder()
                            .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                            .difficultyEnum(DifficultyEnum.ALL)
                            .categoryOpenTDBS(
                                List.of(CategoryOpenTDB.ANIMALS, CategoryOpenTDB.CELEBRITIES))
                            .categoryTriviaApi(List.of())
                            .build())
                    .build())
            .toString());
  }
}
