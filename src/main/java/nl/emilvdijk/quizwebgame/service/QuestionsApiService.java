package nl.emilvdijk.quizwebgame.service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Setter
@Getter
@Service
public class QuestionsApiService {

  private static final String QUIZ_API_URL = "https://the-trivia-api.com/v2/questions";

  private static final String QUIZ_API_URL2 = "https://opentdb.com/api.php?amount=10";

  @Autowired QuestionApiMapperService questionApiMapperService;
  @Autowired UserRepo userRepo;

  /** -- SETTER -- sets uri variables, so we can approach the restapi with arguments. */
  private Map<String, String> quizApiUriVariables = new HashMap<>();

  /**
   * makes call to question api and returns a list of question objects applies api variables if
   * present.
   *
   * @return list of question objects
   */
  public List<Question> getNewQuestions() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user != null) {
      return getDefaultQuestions();
    }
    RestTemplate restTemplate = new RestTemplate();
    MyUser myuser = userRepo.findByUsername(user.getUsername());
    if (quizApiUriVariables.isEmpty()) {

      ResponseEntity<QuestionTriviaApi[]> response =
          restTemplate.getForEntity(QUIZ_API_URL, QuestionTriviaApi[].class);
      return questionApiMapperService.mapQuestions(List.of(response.getBody()));

    } else {

      URI uri =
          UriComponentsBuilder.fromUriString(QUIZ_API_URL)
              .query("categories={categories}")
              .query("difficulties={difficulties}")
              .buildAndExpand(quizApiUriVariables)
              .toUri();
      ResponseEntity<QuestionTriviaApi[]> response =
          restTemplate.getForEntity(uri, QuestionTriviaApi[].class);
      return questionApiMapperService.mapQuestions(List.of(response.getBody()));
    }
  }

  private List<Question> getDefaultQuestions() {
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<QuestionTriviaApi[]> response =
        restTemplate.getForEntity(QUIZ_API_URL, QuestionTriviaApi[].class);
    return questionApiMapperService.mapQuestions(List.of(response.getBody()));
  }
}
