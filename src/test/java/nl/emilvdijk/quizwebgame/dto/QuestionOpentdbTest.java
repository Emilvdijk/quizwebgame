package nl.emilvdijk.quizwebgame.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import nl.emilvdijk.quizwebgame.service.api.QuestionsApiCaller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class QuestionOpentdbTest {

  @Test
  public void testRestTemplate() throws JSONException {
    //    RestTemplate restTemplate = new RestTemplate();
    //
    //    ResponseEntity<String> response =
    //        restTemplate.getForEntity("https://opentdb.com/api.php?amount=10", String.class);
    //
    //    JSONObject jsonob = new JSONObject(response.getBody());
    //    JSONArray json = (JSONArray) jsonob.get("results");
    //
    //    List<QuestionOpentdb> questions = new ArrayList<>();
    //
    //    for (int i = 0; i < json.length(); i++) {
    //      JSONObject block = json.getJSONObject(i);
    //      QuestionOpentdb question = new QuestionOpentdb();
    //      question.setQuestion(block.get("question").toString());
    //      question.setCorrect_answer(block.get("correct_answer").toString());
    //      JSONArray sjonnetje = block.getJSONArray("incorrect_answers");
    //      List<String> listje = new ArrayList<>();
    //      for (int j = 0; j < sjonnetje.length(); j++) {
    //        listje.add(sjonnetje.get(j).toString());
    //      }
    //      question.setIncorrect_answers(listje);
    //      question.setCategory(block.get("category").toString());
    //      question.setDifficulty(block.get("difficulty").toString());
    //      question.setType(block.get("type").toString());
    //      questions.add(question);
    //    }
    //    questions.forEach(questionOpentdb -> System.out.println(questionOpentdb.toString()));

    //    RestTemplate restTemplate = new RestTemplate();
    //
    //    QuestionOpentdb questionOpentdb =
    //        restTemplate.getForObject("https://opentdb.com/api.php?amount=10",
    // QuestionOpentdb.class);
    //    System.out.println(questionOpentdb.toString());

    RestTemplate restTemplate = new RestTemplate();
    URI uri = URI.create("https://opentdb.com/api.php?amount=10");

    //        QuestionOpentdb questionOpentdb =
    //            restTemplate.exchange(uri, HttpMethod.GET, null, QuestionOpentdb.class).getBody();
    //        System.out.println(questionOpentdb.toString());

    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<QuestionOpentdb>() {});
    QuestionOpentdb questionOpentdb123 = (QuestionOpentdb) questionsApiService.getNewQuestion(uri);
    System.out.println(questionOpentdb123.toString());
  }
}
