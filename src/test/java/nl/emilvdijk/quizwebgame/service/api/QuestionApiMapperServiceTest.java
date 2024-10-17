package nl.emilvdijk.quizwebgame.service.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

class QuestionApiMapperServiceTest {

  @Test
  void testHtmlEscapeSpring() {
    System.out.println(
        HtmlUtils.htmlUnescape(
            "The fictional movie &#039;Rochelle, Rochelle&#039; features in which sitcom?"));
  }
}
