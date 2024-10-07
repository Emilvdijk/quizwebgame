package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:applicationTest.properties")
class QuizServiceAuthenticatedTest {
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
  void getQuestionsByChoice() {
    MyUser user = MyUser.builder().apiChoiceEnum(ApiChoiceEnum.OPENTDB).build();
    List<Long> longList = new ArrayList<>();
    longList.add(2L);
    List<Question> questionList =
        questionRepo.findBymyIdNotInAndOrigin(longList, user.getApiChoiceEnum());
    questionList.forEach(question -> assertNotEquals(2L, question.getMyId()));
    questionList.forEach(question -> assertEquals(ApiChoiceEnum.OPENTDB, question.getOrigin()));

    MyUser user2 = MyUser.builder().apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI).build();

    List<Question> questionList2 =
        questionRepo.findBymyIdNotInAndOrigin(longList, user2.getApiChoiceEnum());
    questionList2.forEach(question -> assertNotEquals(2L, question.getMyId()));
    questionList2.forEach(question -> assertEquals(ApiChoiceEnum.TRIVIAAPI, question.getOrigin()));
  }
}
