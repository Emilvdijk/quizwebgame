package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class QuizServiceGuestTest {
  @Autowired QuizServiceGuest quizServiceGuest;
  @MockBean QuestionApiService questionApiService;
  @Autowired QuestionRepo questionRepo;

  @BeforeEach
  void createTestQuestions() {
    List<Question> questionList = new ArrayList<>();
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
    for (long i = 5L; i < 10; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.OPEN_TDB);
      newTestQuestion.setCategory("Entertainment: Video Games");
      newTestQuestion.setDifficulty("medium");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionList.add(newTestQuestion);
    }
    for (long i = 10L; i < 15; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.TRIVIA_API);
      newTestQuestion.setCategory("sport_and_leisure");
      newTestQuestion.setDifficulty("easy");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionList.add(newTestQuestion);
    }
    for (long i = 15L; i < 21; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.TRIVIA_API);
      newTestQuestion.setDifficulty("medium");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionList.add(newTestQuestion);
    }
    questionRepo.saveAll(questionList);
  }

  @Test
  void getNewQuestion() {
    assertNotNull(quizServiceGuest.getNewQuestion());
  }

  @Test
  void addNewQuestionsFromApi(@Autowired QuestionRepo questionRepo) {
    List<Question> newQuestionList = new ArrayList<>();
    for (long i = 21L; i < 31; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.TRIVIA_API);
      newTestQuestion.setDifficulty("medium");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      newQuestionList.add(newTestQuestion);
    }
    when(questionApiService.getDefaultQuestions()).thenReturn(newQuestionList);
    assertEquals(20, questionRepo.count());
    quizServiceGuest.addNewQuestionsFromApi();
    assertEquals(30, questionRepo.count());
  }

  @Test
  void removeAnsweredQuestion() {
    quizServiceGuest.setQuestions(questionRepo.findAll());
    assertEquals(20, quizServiceGuest.questions.size());
    Question question = quizServiceGuest.getQuestions().getFirst();
    assertTrue(quizServiceGuest.questions.contains(question));
    quizServiceGuest.removeAnsweredQuestion();
    assertEquals(19, quizServiceGuest.questions.size());
    assertFalse(quizServiceGuest.questions.contains(question));
  }

  @Test
  void getQuestionById() {
    assertNotNull(quizServiceGuest.getQuestionById(1L));
    assertThrows(Exception.class, () -> quizServiceGuest.getQuestionById(123123L));
  }

  @Test
  void getApplicableRole() {
    assertEquals("ANONYMOUS", quizServiceGuest.getApplicableRole());
  }
}
