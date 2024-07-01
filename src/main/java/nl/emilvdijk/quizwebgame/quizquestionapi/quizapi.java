package nl.emilvdijk.quizwebgame.quizquestionapi;

import nl.emilvdijk.quizwebgame.core.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class quizapi {

  public static void getNewQuestion() {

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Question[]> response = restTemplate.getForEntity(ApiSettings.QUIZ_API_URL,
        Question[].class);
    Question[] questions = response.getBody();



    for (Question question : questions) {
      System.out.println(question.toString());
    }

  }

}
