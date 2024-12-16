package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class RestExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithUserDetails("admin")
  void questionNotFoundHandler() throws Exception {
    mockMvc
        .perform(get("/api/questions/12341234"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("could not find question with id: 12341234")));
  }
}
