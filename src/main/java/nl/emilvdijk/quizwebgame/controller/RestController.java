package nl.emilvdijk.quizwebgame.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.service.RestQuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * controller class for REST api functions.
 *
 * @author Emil van Dijk
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RestController {

  @NonNull private RestQuizService restQuizService;

  @GetMapping("/questions")
  List<Question> findAllQuestions() {
    return restQuizService.findAll();
  }

  @GetMapping("/questions/{id}")
  Question findQuestionById(@PathVariable Long id) throws QuestionNotFoundException {
    return restQuizService.findById(id);
  }
}
