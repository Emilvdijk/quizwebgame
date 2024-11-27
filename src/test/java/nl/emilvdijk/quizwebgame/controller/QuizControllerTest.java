package nl.emilvdijk.quizwebgame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class QuizControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private MockMvc mockMvc;

  @Test
  void showHomePage() throws Exception {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class))
        .contains("QUIZ TIME!!");

    this.mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("QUIZ TIME!!")));
  }

  @Test
  void showQuizQuestion() {}

  @Test
  void questionAnswer() {}

  @Test
  void showQuestionHistory() {}

  //  https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/authentication.html
}
