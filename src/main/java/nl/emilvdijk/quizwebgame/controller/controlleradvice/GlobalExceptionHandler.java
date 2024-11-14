package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import lombok.AllArgsConstructor;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  MyUserService userService;

  @ExceptionHandler(ApiErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String ApiErrorHandler(
      ApiErrorException ex, Model model, @AuthenticationPrincipal MyUser myUser) {
    ResetUserSettings(myUser);
    model.addAttribute("errorMessage", ex.getMessage());
    return "error";
  }

  private void ResetUserSettings(MyUser myUser) {
    userService.resetUserSettings(myUser);
  }
}
