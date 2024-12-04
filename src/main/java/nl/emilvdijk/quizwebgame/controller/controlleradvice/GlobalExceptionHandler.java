package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.client.HttpClientErrorException;
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

  private static final String ERROR_PAGE = "error";
  private static final String ERROR_MESSAGE = "errorMessage";

  @NonNull MyUserService userService;

  @ExceptionHandler(ApiErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String apiErrorHandler(
      ApiErrorException ex, Model model, @AuthenticationPrincipal MyUser myUser) {
    userService.resetUserSettings(myUser);
    model.addAttribute(ERROR_MESSAGE, ex.getMessage());
    return ERROR_PAGE;
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String noResourceFoundExceptionErrorHandler(NoResourceFoundException ex, Model model) {
    model.addAttribute(ERROR_MESSAGE, ex.getMessage());
    return ERROR_PAGE;
  }

  @ExceptionHandler(HttpClientErrorException.class)
  String httpClientErrorExceptionHandler(
      Model model, HttpClientErrorException ex, HttpServletResponse response) {
    response.setStatus(ex.getStatusCode().value());
    model.addAttribute(ERROR_MESSAGE, ex.getMessage());
    return ERROR_PAGE;
  }
}
