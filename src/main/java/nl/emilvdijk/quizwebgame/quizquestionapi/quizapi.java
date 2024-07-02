package nl.emilvdijk.quizwebgame.quizquestionapi;

import java.util.List;
import nl.emilvdijk.quizwebgame.core.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class quizapi {

  public static List<Question> getNewQuestion() {

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Question[]> response =
        restTemplate.getForEntity(ApiSettings.QUIZ_API_URL, Question[].class);
    List<Question> questions = List.of(response.getBody());

    return questions;
  }
}
