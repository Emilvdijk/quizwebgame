package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

class QuestionsApiCallerTest {

  @Test
  void getNewQuestions() {
    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionOpentdb>>() {});
    List<QuestionOpentdb> questionOpentdbList =
        questionsApiService.getNewQuestions(URI.create("https://opentdb.com/api.php?amount=10"));
    questionOpentdbList.forEach(questionOpentdb -> System.out.println(questionOpentdb));
  }
}
