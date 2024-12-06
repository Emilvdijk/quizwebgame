package nl.emilvdijk.quizwebgame.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
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

/**
 * controller class for user based operations.
 *
 * @author Emil van Dijk
 */
@Controller
@Slf4j
@AllArgsConstructor
public class UserController {

  @NonNull MyUserService userService;

  /**
   * shows login page.
   *
   * @return login page
   */
  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  /**
   * shows a new user registration page.
   *
   * @param newMyUser a new user object
   * @return registration page
   */
  @GetMapping("/register")
  public String showRegisterForm(NewMyUser newMyUser) {
    return "register";
  }

  /**
   * post method for registering a new user. also tries to log in the new user immediately
   *
   * @see NewMyUser
   * @param request request object to try and log the new user in immediately
   * @param user NewMyUser dto object to be validated and if all fields are validated to be
   *     registered as a new user
   * @param bindingResult validation object
   * @return redirects to home page
   */
  @PostMapping("/register")
  public String registerUser(
      HttpServletRequest request,
      @Valid NewMyUser user,
      BindingResult bindingResult,
      @AuthenticationPrincipal MyUser myUser) {
    if (!(myUser == null)) {
      return "redirect:/";
    }
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

  /**
   * get method for showing the userPreferences menu.
   *
   * @see UserPreferences
   * @param model model object to add userPreferences object to
   * @param myUser authenticated user to fetch the userPreferences from
   * @return the userPreferences menu page
   */
  @GetMapping("/userPreferences")
  public String userPreferences(Model model, @AuthenticationPrincipal MyUser myUser) {
    MyUser user = userService.loadUserByUsername(myUser.getUsername());
    model.addAttribute("userPreferences", user.getUserPreferences());
    return "userPreferences";
  }

  /**
   * post method for updating the users userPreferences.
   *
   * @see UserPreferences
   * @param userPreferences updated userPreferences from the form
   * @param myUser authenticated user to be updated
   * @return redirects to home page
   */
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

  /**
   * post method for user deleting their account.
   *
   * @param myUser authenticated user to be deleted
   * @param httpSession session to clear of info relating to the user
   * @return redirects to homepage
   */
  @PostMapping("/deleteAccount")
  public String deleteCurrentUser(@AuthenticationPrincipal MyUser myUser, HttpSession httpSession) {
    userService.deleteUserById(myUser.getId());
    SecurityContextHolder.clearContext();
    httpSession.invalidate();
    log.debug("{} deleted their account with user id: {}", myUser.getUsername(), myUser.getId());
    return "redirect:/";
  }

  /**
   * used fetch data about the CategoryOpenTdb enum for thymeleaf to use.
   *
   * @see CategoryOpenTdb
   * @see <a href="https://www.thymeleaf.org/doc/articles/springmvcaccessdata.html">documentation
   *     about model attributes in spring thymeleaf</a>
   * @return list of all enum types in CategoryOpenTdb enum
   */
  @ModelAttribute("allCategoriesOpenTdb")
  public List<CategoryOpenTdb> populateOpenTdbCategories() {
    return Arrays.asList(CategoryOpenTdb.ALL);
  }

  /**
   * used fetch data about the CategoryTriviaApi enum for thymeleaf to use.
   *
   * @see CategoryTriviaApi
   * @see <a href="https://www.thymeleaf.org/doc/articles/springmvcaccessdata.html">documentation
   *     about model attributes in spring thymeleaf</a>
   * @return list of all enum types in CategoryTriviaApi enum
   */
  @ModelAttribute("allCategoriesTriviaApi")
  public List<CategoryTriviaApi> populateTriviaApiCategories() {
    return Arrays.asList(CategoryTriviaApi.ALL);
  }
}
