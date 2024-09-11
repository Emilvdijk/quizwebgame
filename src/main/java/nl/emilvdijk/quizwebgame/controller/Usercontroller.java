package nl.emilvdijk.quizwebgame.controller;

import jakarta.validation.Valid;
import nl.emilvdijk.quizwebgame.Dto.MyUserDto;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Usercontroller {

  @Autowired MyUserService userService;

  /**
   * custom login page
   *
   * @return returns the html login page
   */
  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  /**
   * returns the registration html page so the user can register as a new user
   *
   * @param myUserDto add new user model
   * @return return the registration html page
   */
  @GetMapping("/register")
  public String showRegisterForm(MyUserDto myUserDto) {
    return "register";
  }

  /**
   * post request for the register new user method
   *
   * @param user user to be saved
   * @return returns redirect to the login page
   */
  @PostMapping("/register")
  public String registerUser(@Valid MyUserDto user, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "register";
    }
    userService.registerNewUser(user);
    // TODO explore flash attributes in spring

    //    redirectAttributes.addFlashAttribute("message", "uccesful! Please log in.");
    return "redirect:/login";
  }

  @GetMapping("/authtestpage")
  public String authtestpage() {
    return "authtestpage";
  }
}
