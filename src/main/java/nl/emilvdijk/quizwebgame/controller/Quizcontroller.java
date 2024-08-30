package nl.emilvdijk.quizwebgame.controller;

import java.util.Objects;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class Quizcontroller {

  @Autowired QuizService quizService;

  /**
   * default page. returns homepage.
   *
   * @return home page
   */
  @GetMapping("/")
  public String showHomePage() {
    return "home";
  }

  /**
   * takes the quiz service question and returns quiz page
   *
   * @param model model to add attributes to
   * @return new quiz page
   */
  @GetMapping("/quiz")
  public String showQuizQuestion(Model model) {
    model.addAttribute("question", quizService.getQuestion());
    return "quiz";
  }

  /**
   * this method is called when a user answers a question. it checks if the answer is correct and
   * returns a new result page
   *
   * @param chosenAnswerStr the number of the answer chosen by user
   * @param model model to add attributes to
   * @return either the correct answer html page or the incorrect answer html page
   */
  @PostMapping("/quiz")
  public String questionAnswer(@RequestBody String chosenAnswerStr, Model model) {
    // FIXME fix csrf settings for this page
    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-components
    // https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#integration-with-requestdatavalueprocessor
    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-form

    Question question = quizService.getQuestion();
    model.addAttribute("question", question);
    String chosenAnswer =
        quizService.getQuestion().getAnswers().get(Integer.parseInt(chosenAnswerStr.substring(13)));
    if (Objects.equals(chosenAnswer, quizService.getQuestion().getCorrectAnswer())) {
      quizService.setQuestion(null);
      return "resultpagegood";
    } else {
      quizService.setQuestion(null);
      return "resultpagebad";
    }
  }

  @GetMapping("/login")
  public String login(){
    return "login";
  }
}
