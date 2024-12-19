package nl.emilvdijk.quizwebgame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class QuizControllerTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private MockMvc mockMvc;

  @MockBean QuestionRepo questionRepo;

  @SpyBean MyUserService myUserService;

  List<Question> questionList = new ArrayList<>();

  @BeforeEach
  void setup() {
    for (int i = 1; i < 50; i++) {
      questionList.add(
          Question.builder()
              .questionText("testQuestion" + i)
              .id((long) i)
              .category("testQuestion" + i)
              .correctAnswer("testQuestionAnswer")
              .difficulty("easy" + i)
              .origin(ApiChoiceEnum.TRIVIA_API)
              .incorrectAnswers(List.of())
              .build());
    }
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
    when(questionRepo.findAll()).thenReturn(questionList);
    mockMvc
        .perform(get("/quiz"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("category:")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void showQuizQuestionWithUserAndExpectSuccess() throws Exception {
    when(questionRepo.findAll(any(Specification.class))).thenReturn(questionList);
    mockMvc
        .perform(get("/quiz"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("category:")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  void questionAnswerAnonymousAndExpectSuccess() throws Exception {
    when(questionRepo.findAll(any(Specification.class))).thenReturn(questionList);
    when(questionRepo.findById(anyLong())).thenReturn(Optional.ofNullable(questionList.getFirst()));
    mockMvc
        .perform(post("/quiz").sessionAttr("sessionQuestion", questionList.getFirst()).with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("your answer:")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void questionAnswerUserAndExpectSuccess() throws Exception {
    doNothing().when(myUserService).updateUser(any(MyUser.class));
    when(questionRepo.findById(any(Long.class)))
        .thenReturn(Optional.ofNullable(questionList.getFirst()));
    mockMvc
        .perform(
            post("/quiz")
                .param("chosenAnswer", "testQuestionAnswer")
                .sessionAttr("sessionQuestion", questionList.getFirst())
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Very Well Done!!")))
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
