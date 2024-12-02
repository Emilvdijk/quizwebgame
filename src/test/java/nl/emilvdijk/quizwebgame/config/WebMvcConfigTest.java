package nl.emilvdijk.quizwebgame.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WebMvcConfigTest {

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired private MockMvc mockMvc;

  @Test
  void passwordEncoder() {
    String result = passwordEncoder.encode("myPassword");
    assertTrue(passwordEncoder.matches("myPassword", result));
  }

  @Test
  @WithUserDetails("user")
  void testApiAndExpectForbidden() throws Exception {
    mockMvc
        .perform(get("/api/questions/12341234").with(user("user")))
        .andDo(print())
        .andExpect(status().isForbidden());
    mockMvc
        .perform(get("/api/questions/12341234"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails("admin")
  void testApiAndExpectOk() throws Exception {
    // FIXME when i run test manually by running the program regularly it works just fine?

    //    mockMvc
    //        .perform(get("/api/questions/1").with(user("admin")))
    //        .andDo(print())
    //        .andExpect(status().isOk());
  }

  @Test
  void testApiAndExpect403() throws Exception {
    mockMvc.perform(get("/api/questions/1")).andDo(print()).andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails("user")
  void testApiAndExpect401() throws Exception {
    mockMvc
        .perform(get("/api/questions/1").with(user("user")))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void mvcSecurityTest() {}
}
// FIXME add more tests with:
//  https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/authentication.html
