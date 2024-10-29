package nl.emilvdijk.quizwebgame.controller;

import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.service.DynamicQuizService;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class QuizController {

  MyUserService userService;
  DynamicQuizService dynamicQuizService;

  public QuizController(
      @Autowired MyUserService userService, @Autowired DynamicQuizService dynamicQuizService) {
    this.userService = userService;
    this.dynamicQuizService = dynamicQuizService;
  }

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
    if (user == null) {
      log.debug("Anonymous GET Requested with question Id: {}", question.getId());
    } else {
      log.debug("{} GET Requested with question Id: {}", user.getQuestions(), question.getId());
    }
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
      @ModelAttribute(name = "chosenAnswer") String chosenAnswerStr,
      Model model,
      @AuthenticationPrincipal MyUser user,
      HttpSession httpSession) {
    Question answeredQuestion = (Question) httpSession.getAttribute("question");
    httpSession.removeAttribute("question");

    Question question =
        dynamicQuizService.getService(user).getQuestionByMyid(answeredQuestion.getId());

    if (user != null) {
      userService.markQuestionDone(question, user);
    }

    String chosenAnswer = answeredQuestion.getAnswers().get(Integer.parseInt(chosenAnswerStr));
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
}
