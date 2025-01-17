package nl.emilvdijk.quizwebgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

  @GetMapping("/error403")
  public String error403() {
    return "error403";
  }

  @GetMapping("/error401")
  public String error401() {
    return "error401";
  }
}
