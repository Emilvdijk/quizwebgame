package nl.emilvdijk.quizwebgame.service;

import static org.junit.jupiter.api.Assertions.*;

import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class DynamicQuizServiceTest {
  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

  @Autowired DynamicQuizService dynamicQuizService;

  @Test
  @WithUserDetails("user")
  void getServiceUser() {
    assertEquals(
        "ROLE_USER",
        dynamicQuizService
            .getService(
                (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getApplicableRole());
  }

  @Test
  @WithAnonymousUser
  void getServiceAnonymous() {
    assertEquals("ANONYMOUS", dynamicQuizService.getService(null).getApplicableRole());
  }

  @Test
  @WithUserDetails("admin")
  void getServiceAdmin() {
    assertEquals(
        "ROLE_USER",
        dynamicQuizService
            .getService(
                (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getApplicableRole());
  }
}
