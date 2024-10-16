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
  private RestTemplate restTemplate;

  public QuestionsApiCaller(ParameterizedTypeReference<List<T>> responseType) {
    this.responseType = responseType;
    this.restTemplate = new RestTemplate();
  }

  public List<T> getNewQuestions(URI uri) {
    return restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
  }

  public T getNewQuestion(URI uri) {
    return (T) restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
  }

  public List<QuestionTriviaApi> getDefaultQuestions() {
    ResponseEntity<QuestionTriviaApi[]> response =
        restTemplate.getForEntity(
            "https://the-trivia-api.com/v2/questions", QuestionTriviaApi[].class);
    return List.of(response.getBody());
  }
}
