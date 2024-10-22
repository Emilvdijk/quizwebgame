package nl.emilvdijk.quizwebgame.controller;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import nl.emilvdijk.quizwebgame.service.RESTQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

  private RESTQuizService restQuizService;
  private MyUserService myUserService;

  public RESTController(
      @Autowired RESTQuizService restQuizService, @Autowired MyUserService myUserService) {
    this.restQuizService = restQuizService;
    this.myUserService = myUserService;
  }

  @GetMapping("/rest/questions")
  List<Question> findAllQuestions() {
    return restQuizService.findAll();
  }

  @GetMapping("/rest/questions/{id}")
  Question findQuestionById(@PathVariable Long id) {
    return restQuizService.findById(id);
  }
}
