package nl.emilvdijk.quizwebgame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class QuizControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private MockMvc mockMvc;

  @BeforeAll
  static void setup(@Autowired QuestionRepo questionRepo) {
    List<Question> questionList = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      questionList.add(
          Question.builder()
              .questionText("testQuestion" + i)
              .category("testQuestion" + i)
              .correctAnswer("testQuestionAnswer")
              .difficulty("easy" + i)
              .origin(ApiChoiceEnum.TRIVIA_API)
              .incorrectAnswers(List.of())
              .build());
    }
    questionRepo.saveAll(questionList);
  }

  @Test
  void showHomePageAndExpectSuccess() throws Exception {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class))
        .contains("QUIZ TIME!!");

    this.mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("QUIZ TIME!!")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  void showQuizQuestionAndExpectSuccess() throws Exception {
    mockMvc
        .perform(get("/quiz"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("category:")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  void questionAnswerAnonymousAndExpectSuccess(@Autowired QuestionRepo questionRepo)
      throws Exception {
    mockMvc
        .perform(
            post("/quiz")
                .content("testQuestionAnswer")
                .sessionAttr("sessionQuestion", questionRepo.findById(1L).orElseThrow())
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("your answer:")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void questionAnswerUserAndExpectSuccess(@Autowired QuestionRepo questionRepo) throws Exception {
    mockMvc
        .perform(
            post("/quiz")
                .content("testQuestionAnswer")
                .sessionAttr("sessionQuestion", questionRepo.findById(1L).orElseThrow())
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("your answer:")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void showQuestionHistoryAndExpectSuccess() throws Exception {
    mockMvc
        .perform(get("/questionHistory"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("question")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }
}
