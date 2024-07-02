package nl.emilvdijk.quizwebgame.controller;

import nl.emilvdijk.quizwebgame.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Quizcontroller {

  QuizService quizService;

  @GetMapping("/")
  public String showHomePage(){
return "home";
  }

  @GetMapping("/quiz")
  public String showQuizQuestion(Model model) {
    model.addAttribute("showquizquestion", quizService.getQuestions());
    return "quiz";
  }
}
