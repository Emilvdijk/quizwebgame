package nl.emilvdijk.quizwebgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * base controller for errors.
 *
 * @author Emil van Dijk
 */
@Controller
public class ErrorController {

  /**
   * unauthenticated error page.
   *
   * @return error401 page
   */
  @GetMapping("/error401")
  public String error401() {
    return "error401";
  }

  /**
   * unauthorized/forbidden error page.
   *
   * @return error403 page
   */
  @GetMapping("/error403")
  public String error403() {
    return "error403";
  }
}
