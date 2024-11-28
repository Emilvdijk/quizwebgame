package nl.emilvdijk.quizwebgame;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import nl.emilvdijk.quizwebgame.service.DynamicQuizService;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import nl.emilvdijk.quizwebgame.service.RestQuizService;
import nl.emilvdijk.quizwebgame.service.api.ApiUrlBuilder;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiMapperService;
import nl.emilvdijk.quizwebgame.service.api.QuestionApiService;
import nl.emilvdijk.quizwebgame.service.api.QuestionMapStructMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuizWebGameApplicationTests {

  @Autowired MyUserService userService;
  @Autowired DynamicQuizService dynamicQuizService;
  @Autowired RestQuizService restQuizService;
  @Autowired QuestionApiMapperService questionApiMapperService;
  @Autowired ApiUrlBuilder apiUrlBuilder;
  @Autowired QuestionRepo questionRepo;
  @Autowired QuestionApiService questionApiService;
  @Autowired UserRepo userRepo;
  @Autowired PasswordEncoder passwordEncoder;

  @Value("${myapp.url}")
  String triviaApiUrl;

  @Value("${myapp.url2}")
  String openTdbUrl;

  @Test
  void contextLoads() {
    assertNotNull(userService);
    assertNotNull(dynamicQuizService);
    assertNotNull(
        dynamicQuizService.getService(
            MyUser.builder().username("test").password("test").myRoles(new ArrayList<>()).build()));
    assertNotNull(dynamicQuizService.getService(null));
    assertNotNull(restQuizService);
    assertNotNull(questionApiMapperService);
    assertNotNull(apiUrlBuilder);
    assertNotNull(questionRepo);
    assertNotNull(questionApiService);
    assertNotNull(userRepo);
    assertNotNull(passwordEncoder);
    assertNotNull(triviaApiUrl);
    assertNotNull(openTdbUrl);
    QuestionMapStructMapper instance = QuestionMapStructMapper.INSTANCE;
    assertNotNull(instance);
  }
}
