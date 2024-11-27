package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * class responsible for calling appropriate external api to retrieve quiz questions.
 *
 * @author Emil van Dijk
 * @param <T> type of the appropriate type returned by the external api
 */
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
}
