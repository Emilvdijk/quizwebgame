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
import nl.emilvdijk.quizwebgame.enums.Category;
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
                    .categories(new ArrayList<>())
                    .build())
            .build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }

  /** actually calls api service */
  @Test
  void getNewQuestion() {
    MyUser testUser =
        MyUser.builder()
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categories(new ArrayList<>())
                    .build())
            .build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }

  @Test
  void generateTriviaApiURI()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method generateTriviaApiURIMethod =
        QuestionApiService.class.getDeclaredMethod("generateTriviaApiURI", MyUser.class);
    generateTriviaApiURIMethod.setAccessible(true);

    MyUser user =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                    .difficultyEnum(DifficultyEnum.EASY)
                    .categories(new ArrayList<>())
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
                    .categories(new ArrayList<>())
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
                    .categories(new ArrayList<>())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50",
        generateTriviaApiURIMethod.invoke(questionApiService, user3).toString());
  }

  @Test
  void generateURIOpenTDB()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method generateURIOpenTDBMethod =
        QuestionApiService.class.getDeclaredMethod("generateURIOpenTDB", MyUser.class);
    generateURIOpenTDBMethod.setAccessible(true);

    MyUser user =
        MyUser.builder()
            .username("testUser")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                    .difficultyEnum(DifficultyEnum.EASY)
                    .categories(new ArrayList<>())
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
                    .categories(new ArrayList<>())
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
                    .categories(new ArrayList<>())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50",
        generateURIOpenTDBMethod.invoke(questionApiService, user3).toString());

    // FIXME opentdb only accepts one category per request and enumerated aswell
    assertEquals(
        "https://opentdb.com/api.php?amount=50",
        generateURIOpenTDBMethod
            .invoke(
                questionApiService,
                MyUser.builder()
                    .userPreferences(
                        UserPreferences.builder()
                            .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
                            .difficultyEnum(DifficultyEnum.ALL)
                            .categories(List.of(Category.ANIMALS, Category.CELEBRITIES))
                            .build())
                    .build())
            .toString());
  }
}
