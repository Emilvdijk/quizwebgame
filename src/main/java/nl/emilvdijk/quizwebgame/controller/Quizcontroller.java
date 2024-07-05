package nl.emilvdijk.quizwebgame.controller;

import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Quizcontroller {

  @Autowired QuizService quizService;

  @GetMapping("/")
  public String showHomePage() {
    return "home";
  }

  @GetMapping("/quiz")
  public String showQuizQuestion(Model model) {
    Question question = quizService.getQuestion();
    question.prepareAnswers();
    model.addAttribute("question", question);
    return "quiz";
  }
}
