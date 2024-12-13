package nl.emilvdijk.quizwebgame.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;

class MyUserTest {

  @Test
  void getAuthorities() {
    MyUser testUser =
        MyUser.builder()
            .username("testUser")
            .password("testPassword")
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.ALL)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .build())
            .myRoles(List.of("ROLES_USER"))
            .enabled(true)
            .answeredQuestions(Set.of())
            .build();
    assertEquals(AuthorityUtils.createAuthorityList("ROLES_USER"), testUser.getAuthorities());
  }
}
