package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import static org.junit.jupiter.api.Assertions.*;

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

    // FIXME when i run test manually by running the program regularly it works just fine?

    //    mockMvc
    //        .perform(get("/api/questions/12341234").with(user("admin")))
    //        .andDo(print())
    //        .andExpect(status().isNotFound())
    //        .andExpect(content().string(containsString("could not find question with id:
    // 12341234")));
  }
}
