package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb.QuestionOpentdbQuestion;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.HtmlUtils;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
class QuestionApiMapperServiceTest {

  @Autowired QuestionApiMapperService questionApiMapperService;

  @Test
  void testHtmlEscapeSpring() {
    System.out.println(
        HtmlUtils.htmlUnescape(
            "The fictional movie &#039;Rochelle, Rochelle&#039; features in which sitcom?"));
  }

  @Test
  void mapTriviaApiQuestions() {}

  @Test
  void mapOpenTDBQuestions() {

    List<String> incorrectAnswers = new ArrayList<>();
    incorrectAnswers.add("Majora&#039;s Mask");
    QuestionOpentdbQuestion questionTest =
        QuestionOpentdbQuestion.builder()
            .question(
                "In the game series &#34;The Legend of Zelda&#34;, what was the first 3D game?")
            .correct_answer("Ocarina of Time")
            .incorrect_answers(incorrectAnswers)
            .category("Game")
            .difficulty("easy")
            .build();
    List<QuestionOpentdbQuestion> testQuestionList = new ArrayList<>();
    testQuestionList.add(questionTest);
    QuestionOpentdb questionOpenTdbTest =
        QuestionOpentdb.builder().results(testQuestionList).build();

    List<Question> questionList = questionApiMapperService.mapOpenTDBQuestions(questionOpenTdbTest);
    questionList.forEach(System.out::println);
    assertEquals("Ocarina of Time", questionList.getFirst().getCorrectAnswer());
    assertEquals("Majora's Mask", questionList.getFirst().getIncorrectAnswers().getFirst());
    assertEquals(
        "In the game series \"The Legend of Zelda\", what was the first 3D game?",
        questionList.getFirst().getQuestionText());
  }
}
