package nl.emilvdijk.quizwebgame.api;

import static nl.emilvdijk.quizwebgame.api.ApiSettings.QUIZ_API_URL2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.QuestionTriviaApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class QuizApiTest {

  @Test
  void getNewQuestion() {

    List<Question> questions = QuestionsApi.getNewQuestion();

    assertEquals(10, questions.size());

    for (Question question : questions) {
      System.out.println(question);
    }
  }

  @Test
  void getNewQuestionWithParams() {
    QuestionsApi.quizApiUriVariables.put("difficulties", "easy");
    QuestionsApi.quizApiUriVariables.put("categories", "film_and_tv");

    List<Question> questions = QuestionsApi.getNewQuestion();

    assertEquals(10, questions.size());

    for (Question question : questions) {
      System.out.println(question);
    }
  }

  @Test
  void getNewQuestionsWithOtherApi() throws JSONException {
    // https://opentdb.com/
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.getForEntity(QUIZ_API_URL2, String.class);

    JSONObject jsonob = new JSONObject(response.getBody());
    JSONArray json = (JSONArray) jsonob.get("results");

    List<Question> questions = new ArrayList<>();

    for (int i = 0; i < json.length(); i++) {
      JSONObject block = json.getJSONObject(i);
      QuestionTriviaApi question = new QuestionTriviaApi();
      HashMap<String, String> map = new HashMap<>();
      map.put("text", block.get("question").toString());
      question.setQuestion(map);
      question.setCorrectAnswer(block.get("correct_answer").toString());
      JSONArray sjonnetje = block.getJSONArray("incorrect_answers");
      List<String> listje = new ArrayList<>();
      for (int j = 0; j < sjonnetje.length(); j++) {
        listje.add(sjonnetje.get(j).toString());
      }
      question.setIncorrectAnswers(listje);
      question.setCategory(block.get("category").toString());
      question.setDifficulty(block.get("difficulty").toString());
      question.setType(block.get("type").toString());
      questions.add(question);
    }
    assertEquals(10, questions.size());
    for (Question question : questions) {
      System.out.println(question);
    }
  }
}
