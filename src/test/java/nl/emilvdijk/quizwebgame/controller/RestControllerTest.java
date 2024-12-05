package nl.emilvdijk.quizwebgame.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RestControllerTest {

  @Autowired private MockMvc mockMvc;

  @BeforeAll
  static void setup(@Autowired QuestionRepo questionRepo) {
    questionRepo.save(
        Question.builder()
            .questionText("test")
            .category("test")
            .correctAnswer("test")
            .difficulty("test")
            .incorrectAnswers(List.of())
            .build());
  }

  @Test
  @WithUserDetails("admin")
  void findAllQuestionsAndExpectSuccess() throws Exception {
    mockMvc
        .perform(get("/api/questions/all"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @WithUserDetails("admin")
  void findQuestionByIdAndExpectSuccess() throws Exception {
    mockMvc
        .perform(get("/api/questions/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @WithUserDetails("admin")
  void findQuestionByIdAndExpect404() throws Exception {
    mockMvc
        .perform(get("/api/questions/123452345"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().string("could not find question with id: 123452345"));
  }

  @Test
  @WithUserDetails("admin")
  void findQuestionByIdAndExpect400() throws Exception {
    mockMvc
        .perform(get("/api/questions/1234523452345234523452"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails("user")
  void accessDeniedAndExpectSuccess() throws Exception {
    mockMvc
        .perform(get("/api/questions/all"))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(forwardedUrl("/api/accessDenied"));

    mockMvc
        .perform(get("/api/accessDenied"))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(content().string("You are not allowed to access this resource."));
  }
}
