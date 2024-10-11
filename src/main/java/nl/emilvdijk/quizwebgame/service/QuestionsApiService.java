package nl.emilvdijk.quizwebgame.service;

import java.net.URI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Setter
@Getter
public class QuestionsApiService<T> {

  private static final String QUIZ_API_URL = "https://the-trivia-api.com/v2/questions";

  private static final String QUIZ_API_URL2 = "https://opentdb.com/api.php?amount=10";

  private ParameterizedTypeReference<List<T>> responseType;

  public QuestionsApiService(ParameterizedTypeReference<List<T>> responseType) {
    this.responseType = responseType;
  }

  public List<T> getNewQuestions(MyUser user) {
    RestTemplate restTemplate = new RestTemplate();
    // FIXME add uri builder in another class

    String url;
    switch (user.getUserPreferences().getApiChoiceEnum()) {
      case OPENTDB:
        {
          url = QUIZ_API_URL2;
          break;
        }
      case TRIVIAAPI:
        {
          url = QUIZ_API_URL;
          break;
        }
      default:
        {
          url = QUIZ_API_URL;
          break;
        }
    }
    if (user.getUserPreferences().getQuizApiUriVariables().isEmpty()) {
      return restTemplate.exchange(url, HttpMethod.GET, null, responseType).getBody();
    } else {
      URI uri =
          UriComponentsBuilder.fromUriString(url)
              .query("categories={categories}")
              .query("difficulties={difficulties}")
              .buildAndExpand(user.getUserPreferences().getQuizApiUriVariables())
              .toUri();
      return restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
    }
  }

  public List<QuestionTriviaApi> getDefaultQuestions() {
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<QuestionTriviaApi[]> response =
        restTemplate.getForEntity(QUIZ_API_URL, QuestionTriviaApi[].class);
    return List.of(response.getBody());
  }
}
