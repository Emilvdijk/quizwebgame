package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionsApiCallerTest {

  @Test
  void getNewQuestions() {
    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionOpentdb>>() {});
    List<QuestionOpentdb> questionOpentdbList =
        questionsApiService.getNewQuestions(URI.create("https://opentdb.com/api.php?amount=10"));
    questionOpentdbList.forEach(questionOpentdb -> System.out.println(questionOpentdb));
  }

  /** actually calls api service */
  @Test
  void getNewQuestions2() {
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

  @Test
  public void getNewQuestion() {
    URI uri = URI.create("https://opentdb.com/api.php?amount=10");
    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<QuestionOpentdb>() {});
    QuestionOpentdb questionOpentdb123 = (QuestionOpentdb) questionsApiService.getNewQuestion(uri);
    System.out.println(questionOpentdb123.toString());
  }
}
