package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
class QuizServiceAuthenticatedTest {
  @Autowired QuizServiceAuthenticated quizServiceAuthenticated;

  @BeforeAll
  static void createTestQuestions(@Autowired QuestionRepo questionRepo) {
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
  void getQuestionsByChoice(@Autowired QuestionRepo questionRepo) {
    List<Question> questionList = questionRepo.findAll();
    assertEquals(20, questionList.size());

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.ALL)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(new ArrayList<>())
                .categoryTriviaApiList(new ArrayList<>())
                .build(),
            new ArrayList<>());
    assertEquals(20, questionList.size());

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.EASY)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(new ArrayList<>())
                .categoryTriviaApiList(new ArrayList<>())
                .build(),
            new ArrayList<>());
    assertEquals(9, questionList.size());
    questionList.forEach(
        question -> assertEquals(DifficultyEnum.EASY.getDisplayValue(), question.getDifficulty()));

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.MEDIUM)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(new ArrayList<>())
                .categoryTriviaApiList(new ArrayList<>())
                .build(),
            List.of(4L, 5L, 7L));
    assertEquals(9, questionList.size());
    questionList.forEach(
        question ->
            assertEquals(DifficultyEnum.MEDIUM.getDisplayValue(), question.getDifficulty()));

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.MEDIUM)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(new ArrayList<>())
                .categoryTriviaApiList(new ArrayList<>())
                .build(),
            new ArrayList<>());
    assertEquals(11, questionList.size());
    questionList.forEach(
        question ->
            assertEquals(DifficultyEnum.MEDIUM.getDisplayValue(), question.getDifficulty()));

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.ALL)
                .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                .categoryOpenTdbList(new ArrayList<>())
                .categoryTriviaApiList(new ArrayList<>())
                .build(),
            new ArrayList<>());
    assertEquals(11, questionList.size());
    questionList.forEach(question -> assertEquals(ApiChoiceEnum.TRIVIA_API, question.getOrigin()));

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.ALL)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(List.of(CategoryOpenTdb.ENTERTAINMENT_VIDEO_GAMES))
                .categoryTriviaApiList(List.of())
                .build(),
            new ArrayList<>());
    assertEquals(5, questionList.size());

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.ALL)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(List.of(CategoryOpenTdb.ENTERTAINMENT_FILM))
                .categoryTriviaApiList(List.of())
                .build(),
            new ArrayList<>());
    assertEquals(4, questionList.size());

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.ALL)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(List.of(CategoryOpenTdb.ENTERTAINMENT_FILM))
                .categoryTriviaApiList(List.of())
                .build(),
            List.of(3L));
    assertEquals(3, questionList.size());

    questionList =
        quizServiceAuthenticated.getQuestionsByChoice(
            UserPreferences.builder()
                .difficultyEnum(DifficultyEnum.ALL)
                .apiChoiceEnum(ApiChoiceEnum.ALL)
                .categoryOpenTdbList(List.of(CategoryOpenTdb.ENTERTAINMENT_FILM))
                .categoryTriviaApiList(List.of(CategoryTriviaApi.SPORT_AND_LEISURE))
                .build(),
            List.of(3L));
    assertEquals(8, questionList.size());
    questionList.forEach(
        question ->
            assertTrue(
                question.getCategory().equals("Entertainment: Film")
                    || question.getCategory().equals("sport_and_leisure")));
  }

  @Test
  @WithUserDetails("user")
  void getNewQuestions() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertEquals(0, user.getQuestions().size());
    quizServiceAuthenticated.getNewQuestions();
    assertEquals(20, user.getQuestions().size());
  }
}
