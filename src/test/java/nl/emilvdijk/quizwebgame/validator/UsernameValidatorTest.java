package nl.emilvdijk.quizwebgame.validator;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import nl.emilvdijk.quizwebgame.model.NewMyUser;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class UsernameValidatorTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

  @Autowired Validator validator;

  @Test
  void isValid(@Autowired UserRepo userRepo) {
    NewMyUser testUser = new NewMyUser("okUsername", "okPassword");
    Set<ConstraintViolation<NewMyUser>> violations = validator.validate(testUser);
    assertTrue(violations.isEmpty());

    testUser = new NewMyUser("notOkUsernameItIsWayTooLong", "okPassword");
    violations = validator.validate(testUser);
    assertFalse(violations.isEmpty());

    testUser = new NewMyUser("okUsername", "");
    violations = validator.validate(testUser);
    assertFalse(violations.isEmpty());

    // this username is already in use, so it should find a violation
    assertTrue(userRepo.existsMyUserByUsername("user"));
    testUser = new NewMyUser("user", "password");
    violations = validator.validate(testUser);
    assertFalse(violations.isEmpty());
  }
}
