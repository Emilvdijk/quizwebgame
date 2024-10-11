package nl.emilvdijk.quizwebgame.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nl.emilvdijk.quizwebgame.dto.MyUserDto;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

  @Autowired MyUserService userService;
  Logger logger = LoggerFactory.getLogger(UserController.class);

  /**
   * login page.
   *
   * @return returns the html login page
   */
  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  /**
   * returns the registration html page so the user can register as a new user.
   *
   * @param myUserDto add new user model
   * @return return the registration html page
   */
  @GetMapping("/register")
  public String showRegisterForm(MyUserDto myUserDto) {
    return "register";
  }

  /**
   * post request for the register new user method.
   *
   * @param user user to be saved
   * @return returns redirect to the login page
   */
  @PostMapping("/register")
  public String registerUser(
      HttpServletRequest request, @Valid MyUserDto user, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "register";
    }
    userService.registerNewUser(user);
    try {
      request.login(user.getUsername(), user.getPassword());
    } catch (ServletException e) {
      logger.error("Error while login ", e);
    }
    // TODO explore flash attributes in spring

    //    redirectAttributes.addFlashAttribute("message", "Successful! Please log in.");
    return "redirect:/";
  }

  @GetMapping("/authtestpage")
  public String authtestpage() {
    return "authtestpage";
  }

  @GetMapping("/userPreferences")
  public String userPreferences(Model model, @AuthenticationPrincipal MyUser myUser) {
    model.addAttribute("userPreferences", myUser.getUserPreferences());
    return "userPreferences";
  }

  @PostMapping("/userPreferences")
  public String updateUserPreferences() {
    return "redirect:/";
  }
}
