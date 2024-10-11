package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.model.UserPreferences;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:applicationTest.properties")
class QuizServiceAuthenticatedTest {
  @Autowired QuestionRepo questionRepo;
  @Autowired QuizServiceAuthenticated quizServiceAuthenticated;

  @BeforeAll
  static void createTestQuestions(@Autowired QuestionRepo questionRepo) {
    for (long i = 1L; i < 10; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setMyId(i);
      newTestQuestion.setOrigin(ApiChoiceEnum.OPENTDB);
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionRepo.save(newTestQuestion);
    }

    for (long i = 10L; i < 20; i++) {
      Question newTestQuestion = new Question();
      newTestQuestion.setMyId(i);
      newTestQuestion.setOrigin(ApiChoiceEnum.TRIVIAAPI);
      newTestQuestion.setAnswers(new ArrayList<>());
      newTestQuestion.setIncorrectAnswers(new ArrayList<>());
      questionRepo.save(newTestQuestion);
    }
  }

  @Test
  void getQuestionsByChoice() {
    UserPreferences userPreferences =
        UserPreferences.builder().apiChoiceEnum(ApiChoiceEnum.OPENTDB).build();
    MyUser user = MyUser.builder().userPreferences(userPreferences).build();
    List<Long> longList = new ArrayList<>();
    longList.add(2L);
    List<Question> questionList = quizServiceAuthenticated.getQuestionsByChoice(user, longList);
    questionList.forEach(question -> assertNotEquals(2L, question.getMyId()));
    questionList.forEach(question -> assertEquals(ApiChoiceEnum.OPENTDB, question.getOrigin()));
    assertEquals(8, questionList.size());

    UserPreferences userPreferences2 =
        UserPreferences.builder().apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI).build();
    MyUser user2 = MyUser.builder().userPreferences(userPreferences2).build();

    List<Question> questionList2 = quizServiceAuthenticated.getQuestionsByChoice(user2, longList);
    questionList2.forEach(question -> assertNotEquals(2L, question.getMyId()));
    questionList2.forEach(question -> assertEquals(ApiChoiceEnum.TRIVIAAPI, question.getOrigin()));
    assertEquals(10, questionList2.size());
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
