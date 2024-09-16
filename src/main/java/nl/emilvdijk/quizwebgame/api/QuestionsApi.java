package nl.emilvdijk.quizwebgame.api;

import static nl.emilvdijk.quizwebgame.api.ApiSettings.QUIZ_API_URL;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class QuestionsApi {

  @Setter static Map<String, String> quizApiUriVariables = new HashMap<>();

  /** private constructor to prevent instantiation. */
  private QuestionsApi() {}

  /**
   * makes call to question api and returns a list of question objects applies api variables if
   * present.
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

  /**
   * sets uri variables, so we can approach the restapi with arguments.
   *
   * @param newQuizApiUriVariables new variables map
   */
  static void setQuizApiUriVariables(Map<String, String> newQuizApiUriVariables) {
    quizApiUriVariables = newQuizApiUriVariables;
  }
}
