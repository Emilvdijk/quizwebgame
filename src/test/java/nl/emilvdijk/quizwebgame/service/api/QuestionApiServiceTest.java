package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
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

    UserPreferences userPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
            .quizApiUriVariablesTRIVIAAPI(new HashMap<>())
            .quizApiUriVariablesOPENTDB(new HashMap<>())
            .build();
    MyUser testUser = MyUser.builder().userPreferences(userPreferences).build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }

  /** actually calls api service */
  @Test
  void getNewQuestion() {

    UserPreferences userPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
            .quizApiUriVariablesTRIVIAAPI(new HashMap<>())
            .quizApiUriVariablesOPENTDB(new HashMap<>())
            .build();
    MyUser testUser = MyUser.builder().userPreferences(userPreferences).build();

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
                    .quizApiUriVariablesTRIVIAAPI(Map.of("difficulties", "easy"))
                    .quizApiUriVariablesOPENTDB(new HashMap<>())
                    .build())
            .build();

    assertEquals(
        "https://the-trivia-api.com/v2/questions?limit=50&difficulties=easy",
        generateTriviaApiURIMethod.invoke(questionApiService, user).toString());
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
                    .quizApiUriVariablesTRIVIAAPI(Map.of("difficulties", "easy"))
                    .quizApiUriVariablesOPENTDB(new HashMap<>())
                    .build())
            .build();

    assertEquals(
        "https://opentdb.com/api.php?amount=50",
        generateURIOpenTDBMethod.invoke(questionApiService, user).toString());
  }
}
