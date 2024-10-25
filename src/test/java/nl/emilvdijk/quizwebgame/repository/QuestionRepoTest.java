package nl.emilvdijk.quizwebgame.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionRepoTest {
  @Autowired QuestionRepo questionRepo;

  @BeforeAll
  static void createTestQuestions(@Autowired QuestionRepo questionRepo) {
    Question question = new Question();
    question.setId(1L);
    question.setOrigin(ApiChoiceEnum.TRIVIAAPI);
    questionRepo.save(question);

    Question question2 = new Question();
    question2.setId(2L);
    question2.setOrigin(ApiChoiceEnum.TRIVIAAPI);
    questionRepo.save(question2);

    Question question3 = new Question();
    question3.setId(3L);
    question3.setOrigin(ApiChoiceEnum.OPENTDB);
    questionRepo.save(question3);
  }

  @Test
  void findBymyIdNotIn() {
    List<Long> longList = new ArrayList<>();
    longList.add(2L);
    List<Question> questionList = questionRepo.findByIdNotIn(longList);
    assertFalse(questionList.isEmpty());
    questionList.forEach(question -> assertNotEquals(2L, question.getId()));

    // NotIn derived query will fail if given empty list
    List<Long> longList2 = new ArrayList<>();
    List<Question> questionList2 = questionRepo.findByIdNotIn(longList2);
    assertTrue(questionList2.isEmpty());
  }

  @Test
  void findBymyId() {
    Question question = questionRepo.findById(2L).orElseThrow();
    assertEquals(2L, question.getId());
  }

  @Test
  void findBymyIdNotInAndOrigin() {
    List<Long> longList = new ArrayList<>();
    longList.add(2L);
    List<Question> questionList =
        questionRepo.findByIdNotInAndOrigin(longList, ApiChoiceEnum.OPENTDB);
    questionList.forEach(question -> assertNotEquals(2L, question.getId()));
    questionList.forEach(question -> assertEquals(ApiChoiceEnum.OPENTDB, question.getOrigin()));

    List<Question> questionList2 =
        questionRepo.findByIdNotInAndOrigin(longList, ApiChoiceEnum.TRIVIAAPI);
    questionList2.forEach(question -> assertNotEquals(2L, question.getId()));
    questionList2.forEach(question -> assertEquals(ApiChoiceEnum.TRIVIAAPI, question.getOrigin()));

    // NotIn derived query will fail if given empty list
    List<Long> longList3 = new ArrayList<>();
    List<Question> questionList3 =
        questionRepo.findByIdNotInAndOrigin(longList3, ApiChoiceEnum.TRIVIAAPI);
    assertTrue(questionList3.isEmpty());
  }
}
