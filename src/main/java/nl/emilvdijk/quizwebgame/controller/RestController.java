package nl.emilvdijk.quizwebgame.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.QuestionNotFoundException;
import nl.emilvdijk.quizwebgame.service.RestQuizService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

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

  @GetMapping("/questions/all")
  List<Question> findAllQuestions() {
    return restQuizService.findAll();
  }

  @GetMapping("/questions/{id}")
  Question findQuestionById(@PathVariable Long id) throws QuestionNotFoundException {
    return restQuizService.findById(id);
  }

  @GetMapping("/accessDenied")
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String accessDenied() {
    // FIXME add test
    return "You are not allowed to access this resource.";
  }

  @GetMapping("/unauthorized")
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  String unauthorized() {
    // FIXME add test
    return "You are not authorized to access this resource.";
  }
}
