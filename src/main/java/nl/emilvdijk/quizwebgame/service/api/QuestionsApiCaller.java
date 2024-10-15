package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Setter
@Getter
public class QuestionsApiCaller<T> {

  private ParameterizedTypeReference<List<T>> responseType;

  public QuestionsApiCaller(ParameterizedTypeReference<List<T>> responseType) {
    this.responseType = responseType;
  }

  public List<T> getNewQuestions(URI uri) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
  }

  public T getNewQuestion(URI uri) {
    RestTemplate restTemplate = new RestTemplate();
    return (T) restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
  }

  public List<QuestionTriviaApi> getDefaultQuestions() {
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<QuestionTriviaApi[]> response =
        restTemplate.getForEntity(
            "https://the-trivia-api.com/v2/questions", QuestionTriviaApi[].class);
    return List.of(response.getBody());
  }
}
