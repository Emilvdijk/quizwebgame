package nl.emilvdijk.quizwebgame.api;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class QuestionsApi {

  /** private constructor to prevent instantiation */
  private QuestionsApi() {}

  /**
   * makes call to question api and returns a list of question objects
   *
   * @return list of question objects
   */
  public static List<Question> getNewQuestion() {

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Question[]> response =
        restTemplate.getForEntity(ApiSettings.QUIZ_API_URL, Question[].class);
    return List.of(response.getBody());
  }
}
