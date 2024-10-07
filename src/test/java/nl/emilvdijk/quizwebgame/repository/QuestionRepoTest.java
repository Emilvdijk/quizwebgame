package nl.emilvdijk.quizwebgame.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:applicationTest.properties")
class QuestionRepoTest {
  @Autowired QuestionRepo questionRepo;

  @BeforeAll
  static void createTestQuestions(@Autowired QuestionRepo questionRepo) {
    Question question = new Question();
    question.setMyId(1L);
    question.setOrigin(ApiChoiceEnum.TRIVIAAPI);
    questionRepo.save(question);

    Question question2 = new Question();
    question2.setMyId(2L);
    question2.setOrigin(ApiChoiceEnum.TRIVIAAPI);
    questionRepo.save(question2);

    Question question3 = new Question();
    question3.setMyId(3L);
    question3.setOrigin(ApiChoiceEnum.OPENTDB);
    questionRepo.save(question3);
  }

  @Test
  void findBymyIdNotIn() {
    List<Long> longList = new ArrayList<>();
    longList.add(2L);
    List<Question> questionList = questionRepo.findBymyIdNotIn(longList);
    assertFalse(questionList.isEmpty());
    questionList.forEach(question -> assertNotEquals(2L, question.getMyId()));
  }

  @Test
  void findBymyId() {
    Question question = questionRepo.findBymyId(2L);
    assertEquals(2L, question.getMyId());
  }

  @Test
  void findBymyIdNotInAndOrigin() {
    List<Long> longList = new ArrayList<>();
    longList.add(2L);
    List<Question> questionList =
        questionRepo.findBymyIdNotInAndOrigin(longList, ApiChoiceEnum.OPENTDB);
    questionList.forEach(question -> assertNotEquals(2L, question.getMyId()));
    questionList.forEach(question -> assertEquals(ApiChoiceEnum.OPENTDB, question.getOrigin()));

    List<Question> questionList2 =
        questionRepo.findBymyIdNotInAndOrigin(longList, ApiChoiceEnum.TRIVIAAPI);
    questionList2.forEach(question -> assertNotEquals(2L, question.getMyId()));
    questionList2.forEach(question -> assertEquals(ApiChoiceEnum.TRIVIAAPI, question.getOrigin()));
  }
}
