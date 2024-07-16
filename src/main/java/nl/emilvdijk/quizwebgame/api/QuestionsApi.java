package nl.emilvdijk.quizwebgame.api;

import static nl.emilvdijk.quizwebgame.api.ApiSettings.QUIZ_API_URL;
import static nl.emilvdijk.quizwebgame.api.ApiSettings.quizApiUriVariables;

import java.net.URI;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class QuestionsApi {

  /** private constructor to prevent instantiation */
  private QuestionsApi() {}

  /**
   * makes call to question api and returns a list of question objects applies api variables if
   * present
   *
   * @return list of question objects
   */
  public static List<Question> getNewQuestion() {
    RestTemplate restTemplate = new RestTemplate();

    if (quizApiUriVariables.isEmpty()) {

      ResponseEntity<Question[]> response =
          restTemplate.getForEntity(QUIZ_API_URL, Question[].class);
      return List.of(response.getBody());

    } else {

      URI uri =
          UriComponentsBuilder.fromUriString(QUIZ_API_URL)
              .query("categories={categories}")
              .query("difficulties={difficulties}")
              .buildAndExpand(quizApiUriVariables)
              .toUri();
      ResponseEntity<Question[]> response = restTemplate.getForEntity(uri, Question[].class);
      return List.of(response.getBody());
    }
  }
}
