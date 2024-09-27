package nl.emilvdijk.quizwebgame.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.DynamicQuizService;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class QuizController {

  @Autowired MyUserService userService;
  @Autowired DynamicQuizService dynamicQuizService;

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
   * takes the quiz service question and returns quiz page.
   *
   * @param model model to add attributes to
   * @return new quiz page
   */
  @GetMapping("/quiz")
  public String showQuizQuestion(
      Model model, @AuthenticationPrincipal MyUser user, HttpSession httpSession) {
    Question question = dynamicQuizService.getService(user).getNewQuestion();
    httpSession.setAttribute("question", question);
    model.addAttribute("question", question);
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
  public String questionAnswer(
      @RequestBody String chosenAnswerStr,
      Model model,
      @AuthenticationPrincipal MyUser user,
      HttpSession httpSession) {
    // FIXME fix csrf settings for this page
    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-components
    // https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#integration-with-requestdatavalueprocessor
    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-form

    Question answeredQuestion = (Question) httpSession.getAttribute("question");

    Question question =
        dynamicQuizService.getService(user).getQuestionByMyid(answeredQuestion.getMyId());

    if (user != null) {
      userService.markQuestionDone(question, user);
    }

    String chosenAnswer =
        answeredQuestion.getAnswers().get(Integer.parseInt(chosenAnswerStr.substring(13)));
    dynamicQuizService.getService(user).removeAnsweredQuestion();
    model.addAttribute("question", question);
    model.addAttribute("chosenanswer", chosenAnswer);
    if (Objects.equals(chosenAnswer, question.getCorrectAnswer())) {
      return "resultpagegood";
    } else {
      return "resultpagebad";
    }
  }
}
