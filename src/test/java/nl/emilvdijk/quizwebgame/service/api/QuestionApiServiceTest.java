package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
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
class QuestionApiServiceTest {
  @Autowired QuestionApiService questionApiService;

  /** actually calls api service */
  @Test
  void getNewQuestions() {
    MyUser testUser =
        MyUser.builder()
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTDBS(new ArrayList<>())
                    .categoryTriviaApi(List.of())
                    .build())
            .build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }

  /**
   * actually calls api service. it could fail due to their 5 seconds between api calls rule on
   * their website
   */
  @Test
  void getNewQuestion() {
    MyUser testUser =
        MyUser.builder()
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTDBS(new ArrayList<>())
                    .build())
            .build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }

  @Test
  void generateTriviaApiUri()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method generateTriviaApiURIMethod =
        QuestionApiService.class.getDeclaredMethod("generateTriviaApiUri", MyUser.class);
    generateTriviaApiURIMethod.setAccessible(true);

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
        generateTriviaApiURIMethod.invoke(questionApiService, user).toString());

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
        generateTriviaApiURIMethod.invoke(questionApiService, user2).toString());

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
        generateTriviaApiURIMethod.invoke(questionApiService, user3).toString());
  }

  @Test
  void generateUriOpenTdb()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method generateURIOpenTDBMethod =
        QuestionApiService.class.getDeclaredMethod("generateUriOpenTdb", MyUser.class);
    generateURIOpenTDBMethod.setAccessible(true);

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
        generateURIOpenTDBMethod.invoke(questionApiService, user).toString());

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
        generateURIOpenTDBMethod.invoke(questionApiService, user2).toString());

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
        generateURIOpenTDBMethod.invoke(questionApiService, user3).toString());
    assertEquals(
        "https://opentdb.com/api.php?amount=50&category=27",
        generateURIOpenTDBMethod
            .invoke(
                questionApiService,
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
