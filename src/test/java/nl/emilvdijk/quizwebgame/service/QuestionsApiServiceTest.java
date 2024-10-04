package nl.emilvdijk.quizwebgame.service;

import java.util.HashMap;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

@SpringBootTest
class QuestionsApiServiceTest {

  @Test
  void getNewQuestions() {
    MyUser user =
        MyUser.builder()
            .username("testuser")
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
            .quizApiUriVariables(new HashMap<>())
            .build();
    QuestionsApiService questionsApiService =
        new QuestionsApiService(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    List<QuestionTriviaApi> dtoList = questionsApiService.getNewQuestions(user);
    dtoList.forEach(System.out::println);
  }
}
