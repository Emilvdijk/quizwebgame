package nl.emilvdijk.quizwebgame.api;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class QuestionsApi {

  public static List<Question> getNewQuestion() {

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Question[]> response =
        restTemplate.getForEntity(ApiSettings.QUIZ_API_URL, Question[].class);
    List<Question> questions = List.of(response.getBody());

    return questions;
  }
}
