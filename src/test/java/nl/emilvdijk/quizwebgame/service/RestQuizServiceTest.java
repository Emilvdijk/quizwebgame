package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RestQuizServiceTest {

  @Autowired RestQuizService restQuizService;
  @MockBean QuestionRepo questionRepo;
  List<Question> questionList = new ArrayList<>();

  @BeforeEach
  void createTestQuestions() {
    for (long i = 1L; i < 5; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.OPEN_TDB);
      newTestQuestion.setCategory("Entertainment: Film");
      newTestQuestion.setDifficulty("easy");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionList.add(newTestQuestion);
    }
  }

  @Test
  void findAll() {
    when(questionRepo.findAll()).thenReturn(questionList);
    assertEquals(4, restQuizService.findAll().size());
  }

  @Test
  void findById() {
    when(questionRepo.findById(1L)).thenReturn(Optional.ofNullable(questionList.getFirst()));
    assertNotNull(restQuizService.findById(1L));
  }

  @Test
  void findByIdAndExpectThrows() {
    when(questionRepo.findById(112321231L)).thenReturn(Optional.empty());
    assertThrows(QuestionNotFoundException.class, () -> restQuizService.findById(112321231L));
  }
}
