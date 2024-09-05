package nl.emilvdijk.quizwebgame.controller;

import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Usercontroller {

  @Autowired UserDetailsManager userService;

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
   * @param model add new user model
   * @return return the registration html page
   */
  @GetMapping("/register")
  public String showRegisterForm(Model model) {
    model.addAttribute("user", new MyUser());
    return "register";
  }

  /**
   * post request for the register new user method
   *
   * @param User user to be saved
   * @return returns redirect to the login page
   */
  @PostMapping("register")
  public String registerUser(@ModelAttribute MyUser User) {
    userService.createUser(User);
    // TODO explore flash attributes in spring

    //    redirectAttributes.addFlashAttribute("message", "uccesful! Please log in.");
    return "/login";
  }
}
