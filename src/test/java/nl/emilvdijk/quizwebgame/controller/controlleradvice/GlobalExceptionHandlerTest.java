package nl.emilvdijk.quizwebgame.controller.controlleradvice;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean QuestionApiService questionApiService;

  @Test
  @WithUserDetails("user")
  void noResourceFoundExceptionErrorHandler() throws Exception {
    mockMvc
        .perform(get("/testResourceNotFound").with(user("user")))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("No static resource testResourceNotFound.")));
  }

  @Test
  void noResourceFoundExceptionErrorHandlerAnonymous() throws Exception {
    // if an anonymous user enters an invalid url an error 401 will be thrown and not a 404
    mockMvc
        .perform(get("/testResourceNotFound"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithUserDetails("user")
  void apiErrorHandler() throws Exception {
    when(questionApiService.getNewQuestions(any(MyUser.class)))
        .thenThrow(
            new ApiErrorException(
                "Could not return results. The OpenTdb API doesn't have enough questions for your query."));
    // weirdly enough one of the characters is escaped
    mockMvc
        .perform(get("/quiz"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(
            content()
                .string(
                    containsString(
                        "Could not return results. The OpenTdb API doesn&#39;t have enough questions for your query. your settings have been reset, sorry for the inconvenience.")));
  }
}
