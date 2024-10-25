package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.List;
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

  private ParameterizedTypeReference<List<T>> responseType;
  private RestTemplate restTemplate;

  public QuestionsApiCaller(ParameterizedTypeReference<List<T>> responseType) {
    this.responseType = responseType;
    this.restTemplate = new RestTemplate();
  }

  public List<T> getNewQuestions(URI uri) {
    log.debug("attempting call to: {}", uri);
    ResponseEntity<List<T>> response =
        restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
    log.debug(
        "call made with response code: {} with bodySize: {}",
        response.getStatusCode(),
        response.getBody().size());
    return response.getBody();
  }

  public T getNewQuestion(URI uri) {
    log.debug("attempting call to: {}", uri);
    ResponseEntity<List<T>> response =
        restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
    log.debug("call made with response code: {}", response.getStatusCode());
    return (T) response.getBody();
  }

  public List<QuestionTriviaApi> getDefaultQuestions() {
    log.debug("attempting call to: https://the-trivia-api.com/v2/questions");
    ResponseEntity<QuestionTriviaApi[]> response =
        restTemplate.getForEntity(
            "https://the-trivia-api.com/v2/questions", QuestionTriviaApi[].class);
    log.debug("call made with response code: {}", response.getStatusCode());
    return List.of(response.getBody());
  }
}
