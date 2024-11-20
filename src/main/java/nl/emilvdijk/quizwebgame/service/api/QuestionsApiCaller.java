package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Setter
@Getter
@Slf4j
public class QuestionsApiCaller<T> {

  private ParameterizedTypeReference<T> responseType;
  private RestTemplate restTemplate;

  public QuestionsApiCaller(ParameterizedTypeReference<T> responseType) {
    this.responseType = responseType;
    this.restTemplate = new RestTemplate();
  }

  public T getNewQuestions(URI uri) {
    log.debug("attempting call to: {}", uri);
    ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
    log.debug("call made with response code: {}", response.getStatusCode());
    return response.getBody();
  }

  public T getNewQuestion(URI uri) {
    log.debug("attempting call to: {}", uri);
    ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
    log.debug("call made with response code: {}", response.getStatusCode());
    return response.getBody();
  }

  public List<QuestionTriviaApi> getDefaultQuestions() {
    log.debug("attempting call to: https://the-trivia-api.com/v2/questions?limit=50");
    ResponseEntity<QuestionTriviaApi[]> response =
        restTemplate.getForEntity(
            "https://the-trivia-api.com/v2/questions?limit=50", QuestionTriviaApi[].class);
    log.debug("call made with response code: {}", response.getStatusCode());
    return List.of(Objects.requireNonNull(response.getBody()));
  }
}
