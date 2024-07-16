package nl.emilvdijk.quizwebgame.api;

import static nl.emilvdijk.quizwebgame.api.ApiSettings.quizApiUriVariables;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.junit.jupiter.api.Test;

class quizApiTest {

  @Test
  void getNewQuestion() {

    List<Question> questions = QuestionsApi.getNewQuestion();

    assertEquals(questions.size(), 10);

    for (Question question : questions) {
      System.out.println(question);
    }
  }

  @Test
  void getNewQuestionwithparams() {
    quizApiUriVariables.put("difficulties", "easy");
    quizApiUriVariables.put("categories", "film_and_tv,games");

    List<Question> questions = QuestionsApi.getNewQuestion();

    assertEquals(questions.size(), 10);

    for (Question question : questions) {
      System.out.println(question);
    }
  }
}
