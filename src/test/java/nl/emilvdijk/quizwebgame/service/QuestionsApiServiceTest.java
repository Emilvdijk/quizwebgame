package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:applicationTest.properties")
class QuestionsApiServiceTest {

  /** actually calls api service */
  @Test
  void getNewQuestions() {
    UserPreferences userPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
            .quizApiUriVariables(new HashMap<>())
            .build();
    MyUser user = MyUser.builder().username("testuser").userPreferences(userPreferences).build();
    QuestionsApiService questionsApiService =
        new QuestionsApiService(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    List<QuestionTriviaApi> dtoList = questionsApiService.getNewQuestions(user);
    dtoList.forEach(System.out::println);
    assertEquals(10, dtoList.size());
  }
}
