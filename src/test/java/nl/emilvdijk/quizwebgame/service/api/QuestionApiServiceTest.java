package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
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
            .username("testuser")
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
            .username("testuser")
            .password("test")
            .myRoles(List.of())
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.OPEN_TDB)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTdbList(new ArrayList<>())
                    .build())
            .build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }

  /** actually calls api service */
  @Test
  void getDefaultQuestions() {
    List<Question> questionList = questionApiService.getDefaultQuestions();
    questionList.forEach(System.out::println);
    assertEquals(50, questionList.size());
  }
}
