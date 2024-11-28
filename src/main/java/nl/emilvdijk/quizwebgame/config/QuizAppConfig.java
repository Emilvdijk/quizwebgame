package nl.emilvdijk.quizwebgame.config;

import static nl.emilvdijk.quizwebgame.service.MyUserService.DEFAULT_USER_ROLE;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * global configuration class. thus far only the JpaAuditing is enabled
 *
 * @author Emil van Dijk
 */
@Configuration
@EnableJpaAuditing
@Slf4j
public class QuizAppConfig {

  /**
   * when in test profile "test" or "dev" 2 test users will be added.
   *
   * @param userRepo repo to save users to
   * @param passwordEncoder encoder to use for passwords
   * @return a user service bean
   */
  @Bean
  @Profile({"dev", "test"})
  MyUserService myUserService(
      @Autowired UserRepo userRepo, @Autowired PasswordEncoder passwordEncoder) {
    MyUserService userService = new MyUserService(userRepo, passwordEncoder);
    ArrayList<String> userRoles = new ArrayList<>();
    userRoles.add(DEFAULT_USER_ROLE);
    MyUser testUser =
        MyUser.builder()
            .username("user")
            .password("$2a$10$pJ/ahJVBfkGOjzgyOwZWselKRv6WcsaGFc8Tf1A0VkeUFhpX2jEMG")
            .myRoles(userRoles)
            .enabled(true)
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .build())
            .build();
    userService.saveUser(testUser);

    ArrayList<String> adminRoles = new ArrayList<>();
    adminRoles.add("ROLE_ADMIN");
    adminRoles.add(DEFAULT_USER_ROLE);
    UserPreferences adminPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
            .difficultyEnum(DifficultyEnum.ALL)
            .build();

    MyUser testAdmin =
        MyUser.builder()
            .username("1")
            .password("$2a$10$ixsefZtwnAoLc10H/R6Tu.NBQgWKnhgx5vXs.r2aYp32IjKE6YlCu")
            .myRoles(adminRoles)
            .enabled(true)
            .userPreferences(adminPreferences)
            .build();
    userService.saveUser(testAdmin);
    log.debug("added 2 test users");

    return userService;
  }
}
