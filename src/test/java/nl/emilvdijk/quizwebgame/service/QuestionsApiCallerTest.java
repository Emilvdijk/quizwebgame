package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.service.api.QuestionsApiCaller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:applicationTest.properties")
class QuestionsApiCallerTest {

  /** actually calls api service */
  @Test
  void getNewQuestions() {
    QuestionsApiCaller questionsApiCaller =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    List<QuestionTriviaApi> dtoList =
        questionsApiCaller.getNewQuestions(URI.create("https://the-trivia-api.com/v2/questions"));
    dtoList.forEach(System.out::println);
    assertEquals(10, dtoList.size());

    //    QuestionsApiCaller questionsApiCaller2 =
    //        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionOpentdb>>() {});
    //    List<QuestionTriviaApi> dtoList2 =
    //
    // questionsApiCaller2.getNewQuestions(URI.create("https://opentdb.com/api.php?amount=10"));
    //    dtoList2.forEach(System.out::println);
    //    assertEquals(10, dtoList2.size());
  }
}
