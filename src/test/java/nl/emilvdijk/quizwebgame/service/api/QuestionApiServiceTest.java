package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = QuizWebGameApplication.class)
@TestPropertySource(locations = "classpath:applicationTest.properties")
class QuestionApiServiceTest {

  @Autowired QuestionApiService questionApiService;

  @Test
  void getNewQuestions() {
    UserPreferences userPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.OPENTDB)
            .quizApiUriVariables(new HashMap<>())
            .build();
    MyUser testUser = MyUser.builder().userPreferences(userPreferences).build();

    List<Question> questionList = questionApiService.getNewQuestions(testUser);
    questionList.forEach(question -> System.out.println(question.toString()));
    assertEquals(10, questionList.size());
  }

  @Test
  void getDefaultQuestions() {}
}
