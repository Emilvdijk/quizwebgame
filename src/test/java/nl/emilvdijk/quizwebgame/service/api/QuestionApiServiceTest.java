package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
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

  @Test
  void getNewOpenTdbQuestionsAndExpectError() {
    try (MockedConstruction mocked =
        mockConstruction(
            QuestionsApiCaller.class,
            (questionsApiCaller, _) ->
                when(questionsApiCaller.getNewQuestions(any(URI.class)))
                    .thenReturn(new QuestionOpentdb("1", List.of())))) {
      QuestionsApiCaller<QuestionOpentdb> mockedCaller =
          new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
      assertEquals("1", mockedCaller.getNewQuestions(URI.create("1")).getResponse_code());

      MyUser user =
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

      Exception exception =
          assertThrows(ApiErrorException.class, () -> questionApiService.getNewQuestions(user));
      assertEquals(
          "Could not return results. The OpenTdb API doesn't have enough questions for your query. your settings have been reset, sorry for the inconvenience.",
          exception.getMessage());
    }
  }
}
