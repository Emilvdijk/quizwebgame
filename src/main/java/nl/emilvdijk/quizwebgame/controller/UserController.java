package nl.emilvdijk.quizwebgame.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTDB;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.model.NewMyUser;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

  MyUserService userService;

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  @GetMapping("/register")
  public String showRegisterForm(NewMyUser newMyUser) {
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(
      HttpServletRequest request, @Valid NewMyUser user, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "register";
    }
    userService.registerNewUser(user);
    try {
      request.login(user.getUsername(), user.getPassword());
      log.info("{} logged in", user.getUsername());
    } catch (ServletException e) {
      log.error(
          "Error while logging in after registration with account username: {}",
          user.getUsername(),
          e);
    }
    return "redirect:/";
  }

  @GetMapping("/userPreferences")
  public String userPreferences(Model model, @AuthenticationPrincipal MyUser myUser) {
    MyUser user = userService.loadUserByUsername(myUser.getUsername());
    model.addAttribute("userPreferences", user.getUserPreferences());
    return "userPreferences";
  }

  @PostMapping("/userPreferences")
  public String updateUserPreferences(
      @ModelAttribute(name = "userPreferences") UserPreferences userPreferences,
      @AuthenticationPrincipal MyUser myUser) {
    myUser.setUserPreferences(userPreferences);
    myUser.setQuestions(new ArrayList<>());
    MyUser user = userService.loadUserByUsername(myUser.getUsername());
    user.setUserPreferences(userPreferences);
    userService.updateUser(user);
    log.debug(
        "{} updated their user preferences with UserPreferences id: {}",
        user.getUsername(),
        user.getUserPreferences().getId());
    return "redirect:/";
  }

  @PostMapping("/deleteAccount")
  public String deleteCurrentUser(@AuthenticationPrincipal MyUser myUser, HttpSession httpSession) {
    userService.deleteUserById(myUser.getId());
    SecurityContextHolder.clearContext();
    httpSession.invalidate();
    log.debug("{} deleted their account with user id: {}", myUser.getUsername(), myUser.getId());
    return "redirect:/";
  }

  @GetMapping("/authtestpage")
  public String authtestpage() {
    return "authtestpage";
  }

  @ModelAttribute("allCategoriesOpenTDB")
  public List<CategoryOpenTDB> populateOpenTDBCategories() {
    return Arrays.asList(CategoryOpenTDB.ALL);
  }

  @ModelAttribute("allCategoriesTriviaApi")
  public List<CategoryTriviaApi> populateTriviaApiCategories() {
    return Arrays.asList(CategoryTriviaApi.ALL);
  }
}
