package nl.emilvdijk.quizwebgame.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.emilvdijk.quizwebgame.QuizWebGameApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = QuizWebGameApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class WebMvcConfigTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired MockMvc mockMvc;

  //     mockMvc.perform(get("/").with(user("user"))) doesn't work!

  @Test
  void passwordEncoder() {
    String result = passwordEncoder.encode("myPassword");
    assertTrue(passwordEncoder.matches("myPassword", result));
  }

  @Test
  void testHomeAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(forwardedUrl(null))
        .andExpect(redirectedUrl(null));
  }

  @Test
  @WithUserDetails("admin")
  void testHistoryAndExpectOkAdmin() throws Exception {
    mockMvc.perform(get("/questionHistory")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  @WithUserDetails("user")
  void testHistoryAndExpectOkUser() throws Exception {
    mockMvc.perform(get("/questionHistory")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void testHistoryAndExpectUnauthorized() throws Exception {
    mockMvc
        .perform(get("/questionHistory"))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(forwardedUrl("/error401"));
  }

  @Test
  @WithUserDetails("admin")
  void testApiAndExpectOk() throws Exception {
    mockMvc.perform(get("/api/questions/all")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void testApiAndExpectOkUsingHeaders() throws Exception {
    mockMvc
        .perform(
            get("/api/questions/all")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "1")))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void testApiAndExpect401() throws Exception {
    mockMvc
        .perform(get("/api/questions/all"))
        .andDo(print())
        .andExpect(
            result ->
                assertEquals(
                    "You are not authorized to access this resource.",
                    result.getResponse().getContentAsString()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails("user")
  void testApiAndExpect403() throws Exception {
    mockMvc
        .perform(get("/api/questions/all"))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(forwardedUrl("/api/accessDenied"));
  }

  @Test
  void questionAnswerCsrfExpectFail() throws Exception {
    mockMvc
        .perform(post("/quiz").with(csrf().useInvalidToken()))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails("admin")
  void testActuatorAndExpectOk() throws Exception {
    mockMvc.perform(get("/actuator/health")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void testActuatorAndExpect401() throws Exception {
    mockMvc
        .perform(get("/actuator/health"))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(forwardedUrl("/error401"));
  }

  @Test
  @WithUserDetails("user")
  void testActuatorAndExpect403() throws Exception {
    mockMvc
        .perform(get("/actuator/health"))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(forwardedUrl("/error403"));
  }
}
