package nl.emilvdijk.quizwebgame.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import nl.emilvdijk.quizwebgame.service.RESTQuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller class for REST api functions.
 *
 * @author Emil van Dijk
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RESTController {

  private RESTQuizService restQuizService;
  private MyUserService myUserService;

  @GetMapping("/questions")
  List<Question> findAllQuestions() {
    return restQuizService.findAll();
  }

  @GetMapping("/questions/{id}")
  Question findQuestionById(@PathVariable Long id) throws QuestionNotFoundException {
    return restQuizService.findById(id);
  }
}
