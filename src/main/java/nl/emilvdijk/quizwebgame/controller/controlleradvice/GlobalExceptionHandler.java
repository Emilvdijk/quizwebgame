package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * global error handler class. all custom errors and special operations for handling exceptions are
 * configured here.
 *
 * @author Emil van Dijk
 */
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  @NonNull MyUserService userService;

  @ExceptionHandler(ApiErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String apiErrorHandler(
      ApiErrorException ex, Model model, @AuthenticationPrincipal MyUser myUser) {
    resetUserSettings(myUser);
    model.addAttribute("errorMessage", ex.getMessage());
    return "error";
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String noResourceFoundExceptionErrorHandler(NoResourceFoundException ex, Model model) {
    // FIXME add test for error
    model.addAttribute("errorMessage", ex.getMessage());
    return "error";
  }

  private void resetUserSettings(MyUser myUser) {
    userService.resetUserSettings(myUser);
  }
}
