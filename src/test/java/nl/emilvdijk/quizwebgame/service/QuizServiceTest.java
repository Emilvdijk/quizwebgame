package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = QuizWebGameApplication.class)
class QuizServiceTest {

  @Autowired
  QuizServiceAuthenticated quizService;

  @Test
  void getQuestion() {
    List<Question> nice2 = quizService.questionRepo.findAll();
    for (Question question : nice2) {
      System.out.println(question);
    }

    ArrayList<String> roles = new ArrayList<>();
    roles.add("TESTROLE");
    MyUser myUser =
        MyUser.builder()
            .id(1L)
            .username("user")
            .password("user")
            .myRoles(roles)
            .enabled(true)
            .build();
    List<Long> idlist = new ArrayList<>();
    idlist.add(myUser.getId());
    List<Question> nice = quizService.questionRepo.findByMyidNotIn(idlist);
    for (Question question : nice) {
      System.out.println(question);
    }
  }

  @Test
  void setQuestion() {}
}
