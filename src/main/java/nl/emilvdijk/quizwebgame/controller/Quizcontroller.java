package nl.emilvdijk.quizwebgame.controller;

import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    model.addAttribute("question", question);
    return "quiz";
  }

//  @GetMapping("/quiz/{number}")


  @PostMapping("/quiz")
  public String questionAnswer(@RequestBody String numbersrt){
    int number = Integer.valueOf(numbersrt.substring(7));
    if(Objects.equals(quizService.getQuestion().getAnswers().get(number), quizService.getQuestion().getCorrectAnswer())){
      quizService.setQuestion(null);
      return "resultpagegood";
    }else {
      quizService.setQuestion(null);
      return "resultpagebad";
    }
  }
}
