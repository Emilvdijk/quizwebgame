package nl.emilvdijk.quizwebgame.service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApiDto;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Setter
@Service
public class QuestionsApiService {
  static final String QUIZ_API_URL = "https://the-trivia-api.com/v2/questions";

  static final String QUIZ_API_URL2 = "https://opentdb.com/api.php?amount=10";

  @Autowired QuestionApiMapperService questionApiMapperService;

  /** -- SETTER -- sets uri variables, so we can approach the restapi with arguments. */
  private Map<String, String> quizApiUriVariables = new HashMap<>();

  /**
   * makes call to question api and returns a list of question objects applies api variables if
   * present.
   *
   * @return list of question objects
   */
  public List<Question> getNewQuestion() {
    RestTemplate restTemplate = new RestTemplate();

    if (quizApiUriVariables.isEmpty()) {

      ResponseEntity<QuestionTriviaApiDto[]> response =
          restTemplate.getForEntity(QUIZ_API_URL, QuestionTriviaApiDto[].class);
      return questionApiMapperService.mapQuestion(List.of(response.getBody()));

    } else {

      URI uri =
          UriComponentsBuilder.fromUriString(QUIZ_API_URL)
              .query("categories={categories}")
              .query("difficulties={difficulties}")
              .buildAndExpand(quizApiUriVariables)
              .toUri();
      ResponseEntity<QuestionTriviaApiDto[]> response =
          restTemplate.getForEntity(uri, QuestionTriviaApiDto[].class);
      return questionApiMapperService.mapQuestion(List.of(response.getBody()));
    }
  }
}
