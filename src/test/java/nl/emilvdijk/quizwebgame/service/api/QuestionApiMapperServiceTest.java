package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb.QuestionOpentdbQuestion;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class QuestionApiMapperServiceTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

  @Autowired QuestionApiMapperService questionApiMapperService;

  @Test
  void mapTriviaApiQuestions() {
    QuestionTriviaApi triviaApi = new QuestionTriviaApi();
    triviaApi.setCategory("arts_and_literature");
    triviaApi.setQuestion(Map.of("text", "Which author wrote 'Dune novel series'?"));
    triviaApi.setCorrectAnswer("Frank Herbert");
    triviaApi.setIncorrectAnswers(
        List.of("Ursula K. Le Guin", "H. P. Lovecraft", "Edgar Rice Burroughs"));
    triviaApi.setTags(List.of("arts_and_literature"));
    triviaApi.setType("text_choice");
    triviaApi.setDifficulty("hard");
    triviaApi.setRegions(List.of());
    triviaApi.setIsNiche("false");
    List<Question> questionList =
        questionApiMapperService.mapTriviaApiQuestions(List.of(triviaApi));
    System.out.println(questionList.getFirst().toString());
    assertEquals(
        "Question(questionText=Which author wrote 'Dune novel series'?, correctAnswer=Frank Herbert, incorrectAnswers=[Ursula K. Le Guin, H. P. Lovecraft, Edgar Rice Burroughs], category=arts_and_literature, tags=[arts_and_literature], type=text_choice, difficulty=hard, answers=null, lastUpdatedOn=null, origin=TRIVIA_API)",
        questionList.getFirst().toString());
  }

  @Test
  void mapOpenTdbQuestions() {
    QuestionOpentdbQuestion questionTest =
        QuestionOpentdbQuestion.builder()
            .question(
                "In the game series &#34;The Legend of Zelda&#34;, what was the first 3D game?")
            .correct_answer("Ocarina of Time")
            .incorrect_answers(List.of("Majora&#039;s Mask"))
            .category("Game")
            .difficulty("easy")
            .build();
    List<QuestionOpentdbQuestion> testQuestionList = new ArrayList<>();
    testQuestionList.add(questionTest);
    QuestionOpentdb questionOpenTdbTest =
        QuestionOpentdb.builder().results(testQuestionList).build();

    List<Question> questionList = questionApiMapperService.mapOpenTdbQuestions(questionOpenTdbTest);
    questionList.forEach(System.out::println);
    assertEquals("Ocarina of Time", questionList.getFirst().getCorrectAnswer());
    assertEquals("Majora's Mask", questionList.getFirst().getIncorrectAnswers().getFirst());
    assertEquals(
        "In the game series \"The Legend of Zelda\", what was the first 3D game?",
        questionList.getFirst().getQuestionText());
  }
}
