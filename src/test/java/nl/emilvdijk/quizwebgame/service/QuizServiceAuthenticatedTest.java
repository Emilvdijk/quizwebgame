package nl.emilvdijk.quizwebgame.service;

import static nl.emilvdijk.quizwebgame.entity.Question.DifficultyEquals;
import static nl.emilvdijk.quizwebgame.entity.Question.IdNotIn;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuizServiceAuthenticatedTest {
  @Autowired QuizServiceAuthenticated quizServiceAuthenticated;

  @BeforeAll
  static void createTestQuestions(@Autowired QuestionRepo questionRepo) {
    for (long i = 1L; i < 5; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.OPENTDB);
      newTestQuestion.setDifficulty("easy");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionRepo.save(newTestQuestion);
    }
    for (long i = 5L; i < 10; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.OPENTDB);
      newTestQuestion.setDifficulty("medium");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionRepo.save(newTestQuestion);
    }
    for (long i = 10L; i < 15; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.TRIVIAAPI);
      newTestQuestion.setDifficulty("easy");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionRepo.save(newTestQuestion);
    }
    for (long i = 15L; i < 20; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setId(i);
      newTestQuestion.setQuestionText(String.valueOf(i));
      newTestQuestion.setOrigin(ApiChoiceEnum.TRIVIAAPI);
      newTestQuestion.setDifficulty("medium");
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionRepo.save(newTestQuestion);
    }
  }

  @Test
  void getQuestionsByChoice(@Autowired QuestionRepo questionRepo) {
    //    UserPreferences userPreferences =
    //        UserPreferences.builder().apiChoiceEnum(ApiChoiceEnum.OPENTDB).build();
    //    MyUser user = MyUser.builder().userPreferences(userPreferences).build();
    //    List<Long> longList = new ArrayList<>();
    //    longList.add(2L);
    //    List<Question> questionList = quizServiceAuthenticated.getQuestionsByChoice(user,
    // longList);
    //    questionList.forEach(question -> assertNotEquals(2L, question.getId()));
    //    questionList.forEach(question -> assertEquals(ApiChoiceEnum.OPENTDB,
    // question.getOrigin()));
    //    assertEquals(8, questionList.size());
    //
    //    UserPreferences userPreferences2 =
    //        UserPreferences.builder().apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI).build();
    //    MyUser user2 = MyUser.builder().userPreferences(userPreferences2).build();
    //
    //    List<Question> questionList2 = quizServiceAuthenticated.getQuestionsByChoice(user2,
    // longList);
    //    questionList2.forEach(question -> assertNotEquals(2L, question.getId()));
    //    questionList2.forEach(question -> assertEquals(ApiChoiceEnum.TRIVIAAPI,
    // question.getOrigin()));
    //    assertEquals(10, questionList2.size());

    List<Question> questionList =
        questionRepo.findAll(where(DifficultyEquals(DifficultyEnum.EASY)));

    questionList.forEach(System.out::println);

    System.out.println();
    questionList = questionRepo.findAll();

    questionList.forEach(System.out::println);

    System.out.println();

    questionList =
        questionRepo.findAll(
            where(DifficultyEquals(DifficultyEnum.MEDIUM)).and(IdNotIn(List.of(4L, 5L, 7L))));

    questionList.forEach(System.out::println);

    questionList =
        questionRepo.findAll(
            where(DifficultyEquals(DifficultyEnum.MEDIUM)).and(IdNotIn(new ArrayList<>())));

    questionList.forEach(System.out::println);
  }

  @Test
  @WithUserDetails("user")
  void getNewQuestions() {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertEquals(0, user.getQuestions().size());
    quizServiceAuthenticated.getNewQuestions();
    assertEquals(10, user.getQuestions().size());
  }
}
