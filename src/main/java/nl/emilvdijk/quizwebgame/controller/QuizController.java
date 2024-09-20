package nl.emilvdijk.quizwebgame.controller;

import java.util.Objects;

import nl.emilvdijk.quizwebgame.dto.QuestionDto;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import nl.emilvdijk.quizwebgame.service.QuizServiceAuthenticated;
import nl.emilvdijk.quizwebgame.service.QuizServiceGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class QuizController {

  @Autowired MyUserService userService;
  @Autowired QuizServiceAuthenticated quizServiceAuthenticated;
  @Autowired QuizServiceGuest quizServiceGuest;

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
  public String showQuizQuestion(Model model, @AuthenticationPrincipal MyUser user) {
    if (user == null) {
      model.addAttribute("question", quizServiceGuest.getNewQuestion());
    } else {
      model.addAttribute("question", quizServiceAuthenticated.getNewQuestion());
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
  public String questionAnswer(@RequestBody String chosenAnswerStr, Model model) {
    // FIXME fix csrf settings for this page
    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-components
    // https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#integration-with-requestdatavalueprocessor
    // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-form
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    QuestionDto questionDto;
    String chosenAnswer;
    Question question;
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      questionDto = quizServiceAuthenticated.getNewQuestion();
      MyUser currentAuthUser = (MyUser) authentication.getPrincipal();
      MyUser myUser = userService.loadUserByUsername(currentAuthUser.getUsername());
      question = quizServiceAuthenticated.getQuestionByMyid(questionDto.getMyId());
      userService.markQuestionDone(question, myUser);
      chosenAnswer =
              questionDto
              .getAnswers()
              .get(Integer.parseInt(chosenAnswerStr.substring(13)));
      quizServiceAuthenticated.removeAnsweredQuestion();
    } else {
      questionDto = quizServiceGuest.getNewQuestion();
      question = quizServiceAuthenticated.getQuestionByMyid(questionDto.getMyId());
      chosenAnswer =
              questionDto
              .getAnswers()
              .get(Integer.parseInt(chosenAnswerStr.substring(13)));
      quizServiceGuest.removeAnsweredQuestion();
    }

    model.addAttribute("question", question);
    model.addAttribute("chosenanswer", chosenAnswer);
    if (Objects.equals(chosenAnswer, question.getCorrectAnswer())) {
      return "resultpagegood";
    } else {
      return "resultpagebad";
    }
  }
}
