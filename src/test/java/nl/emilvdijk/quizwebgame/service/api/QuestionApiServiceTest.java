package nl.emilvdijk.quizwebgame.service.api;

import static nl.emilvdijk.quizwebgame.service.MyUserService.DEFAULT_USER_ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
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

  @Test
  void testGetNewQuestions(@Autowired ApiUrlBuilder apiUrlBuilder) {
    MyUser testUser =
        MyUser.builder()
            .username("testuser")
            .password("testpass")
            .myRoles(List.of(DEFAULT_USER_ROLE))
            .enabled(true)
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .categoryOpenTdbList(List.of())
                    .categoryTriviaApiList(List.of())
                    .build())
            .build();

    URI uri = apiUrlBuilder.generateTriviaApiUri(testUser);
    try (MockedConstruction<QuestionsApiCaller> mock = mockConstruction(QuestionsApiCaller.class)) {
      // FIXME change behavior of code so it can be tested

      QuestionTriviaApi triviaApiTestObject = new QuestionTriviaApi();
      triviaApiTestObject.setCategory("arts_and_literature");
      triviaApiTestObject.setQuestion(Map.of("text", "Which author wrote 'Dune novel series'?"));
      triviaApiTestObject.setCorrectAnswer("Frank Herbert");
      triviaApiTestObject.setIncorrectAnswers(
          List.of("Ursula K. Le Guin", "H. P. Lovecraft", "Edgar Rice Burroughs"));
      triviaApiTestObject.setTags(List.of("arts_and_literature"));
      triviaApiTestObject.setType("text_choice");
      triviaApiTestObject.setDifficulty("hard");
      triviaApiTestObject.setRegions(List.of());
      triviaApiTestObject.setIsNiche("false");

      when(questionsApiCaller.getNewQuestions(uri)).thenReturn(List.of(triviaApiTestObject));

      List<Question> questionList = questionApiService.getNewQuestions(testUser);
      System.out.println(questionList);
    }
  }
}
