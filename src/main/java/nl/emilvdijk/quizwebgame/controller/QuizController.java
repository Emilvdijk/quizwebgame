package nl.emilvdijk.quizwebgame.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.DynamicQuizService;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * base controller class for the program website. several services have been attached.
 *
 * @see MyUserService
 * @see DynamicQuizService
 * @author Emil van Dijk
 */
@Controller
@Slf4j
@AllArgsConstructor
public class QuizController {

  @NonNull MyUserService userService;
  @NonNull DynamicQuizService dynamicQuizService;

  /**
   * shows homepage.
   *
   * @return homepage
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
    httpSession.setAttribute("sessionQuestion", question);
    model.addAttribute("question", question);
    if (user == null) {
      log.debug("Anonymous GET Requested with question Id: {}", question.getId());
    } else {
      log.debug("{} GET Requested with question Id: {}", user.getUsername(), question.getId());
    }
    return "quiz";
  }

  /**
   * this method is called when a user answers a question. it checks if the answer is correct and
   * returns a new result page
   *
   * @param chosenAnswer the answer chosen by user
   * @param model model to add attributes to
   * @return either the correct answer html page or the incorrect answer html page
   */
  @PostMapping("/quiz")
  public String questionAnswer(
      @ModelAttribute(name = "chosenAnswer") String chosenAnswer,
      Model model,
      @AuthenticationPrincipal MyUser user,
      HttpSession httpSession) {
    Question answeredQuestion = (Question) httpSession.getAttribute("sessionQuestion");
    httpSession.removeAttribute("sessionQuestion");

    Question question =
        dynamicQuizService.getService(user).getQuestionById(answeredQuestion.getId());
    if (user != null) {
      userService.markQuestionDone(question, user, chosenAnswer);
    }
    dynamicQuizService.getService(user).removeAnsweredQuestion();
    model.addAttribute("question", question);
    model.addAttribute("chosenanswer", chosenAnswer);
    if (user == null) {
      log.debug("Anonymous POST Requested with question Id: {}", question.getId());
    } else {
      log.debug("{} POST Requested with question Id: {}", user.getUsername(), question.getId());
    }
    if (Objects.equals(chosenAnswer, question.getCorrectAnswer())) {
      return "resultpagegood";
    } else {
      return "resultpagebad";
    }
  }

  /**
   * shows question history page.
   *
   * @param model model object to add answered questions to
   * @param user user to retrieve answered questions from
   * @return question history page
   */
  @GetMapping("/questionHistory")
  public String showQuestionHistory(Model model, @AuthenticationPrincipal MyUser user) {
    MyUser myUser = userService.loadUserByUsername(user.getUsername());
    model.addAttribute("answeredQuestions", userService.getSortedAnsweredQuestions(myUser));
    return "questionHistory";
  }

  /**
   * mapping for access denied page.
   *
   * @return access denied page
   */
  @GetMapping("/error403")
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public String accessDenied() {
    return "/error403";
  }

  /**
   * mapping for unauthorized page.
   *
   * @return unauthorized page
   */
  @GetMapping("/error401")
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String unauthorized() {
    return "/error401";
  }
}
