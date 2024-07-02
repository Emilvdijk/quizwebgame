package nl.emilvdijk.quizwebgame.quizquestionapi;

import nl.emilvdijk.quizwebgame.core.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class quizapiTest {

  @Test
  void getNewQuestion() {
    List<Question> questions = quizapi.getNewQuestion();
    for (Question question : questions) {
      System.out.println(question);
    }
  }
}
