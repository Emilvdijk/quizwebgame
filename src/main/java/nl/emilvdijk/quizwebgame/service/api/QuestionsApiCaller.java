package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import lombok.Getter;
import lombok.NonNull;
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

  @NonNull private ParameterizedTypeReference<T> responseType;
  @NonNull private RestTemplate restTemplate;

  /**
   * default constructor for class.
   *
   * @param responseType response type of the object retrieves from the api
   */
  public QuestionsApiCaller(@NonNull ParameterizedTypeReference<T> responseType) {
    this.responseType = responseType;
    this.restTemplate = new RestTemplate();
  }

  /**
   * call to the api using the given uri and return objects of given type.
   *
   * @param uri uri to call the api
   * @return returns object of given response type
   */
  public T getNewQuestions(URI uri) {
    log.debug("attempting call to: {}", uri);
    ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, null, responseType);
    log.debug("call made with response code: {}", response.getStatusCode());
    return response.getBody();
  }
}
