package nl.emilvdijk.quizwebgame.controller;

import static nl.emilvdijk.quizwebgame.service.MyUserService.DEFAULT_USER_ROLE;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {
  @Autowired private MockMvc mockMvc;

  @BeforeAll
  static void setup(@Autowired UserRepo userRepo) {
    MyUser testUser =
        MyUser.builder()
            .username("testUserForDelete")
            .password("$2a$10$pJ/ahJVBfkGOjzgyOwZWselKRv6WcsaGFc8Tf1A0VkeUFhpX2jEMG")
            .myRoles(List.of(DEFAULT_USER_ROLE))
            .enabled(true)
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .build())
            .build();
    userRepo.save(testUser);

    MyUser testUser2 =
        MyUser.builder()
            .username("testUserForPreferences")
            .password("$2a$10$pJ/ahJVBfkGOjzgyOwZWselKRv6WcsaGFc8Tf1A0VkeUFhpX2jEMG")
            .myRoles(List.of(DEFAULT_USER_ROLE))
            .enabled(true)
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .build())
            .build();
    userRepo.save(testUser2);
  }

  @Test
  void showLoginFormAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/login"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Please Log In")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void showLoginFormAuthenticatedAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/login"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Please Log In")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  void showRegisterFormAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/register"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("new user registration")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void showRegisterFormAuthenticatedAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/register"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("new user registration")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  void registerUserAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/register"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("new user registration")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("user")
  void postRegisterUserAuthenticatedAndExpectRedirect(@Autowired UserRepo userRepo)
      throws Exception {
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=testRegisterUsername&password=TestRegisterPassword")
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
    assertFalse(userRepo.existsMyUserByUsername("testRegisterUsername"));
  }

  @Test
  void postRegisterUserAndExpectSuccess(@Autowired UserRepo userRepo) throws Exception {
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=testRegUsername2&password=TestRegisterPassword2")
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
    assertNotNull(userRepo.findByUsername("testRegUsername2").orElseThrow());
  }

  @Test
  void postRegisterUserAndExpectBindingResultErrorTooLong(@Autowired UserRepo userRepo)
      throws Exception {
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=testRegUsername2ButItIsTooLong&password=TestRegisterPassword2")
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("needs to be between 3 and 20 characters")));
    assertNull(userRepo.findByUsername("testRegUsername2ButItIsTooLong").orElse(null));
  }

  @Test
  void postRegisterUserAndExpectBindingResultErrorUserAlreadyExists(@Autowired UserRepo userRepo)
      throws Exception {
    assertNotNull(userRepo.findByUsername("user").orElseThrow());
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=user&password=TestRegisterPassword")
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("username already exists")));
  }

  @Test
  void postRegisterUserCsrfTest() throws Exception {
    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("username=testRegUsername3&password=TestRegisterPassword3")
                .with(csrf().useInvalidToken()))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(forwardedUrl("/error403"));
  }

  @Test
  @WithUserDetails("user")
  void userPreferencesAndExpectOk() throws Exception {
    mockMvc
        .perform(get("/userPreferences"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("welcome to user preferences")))
        .andExpect(content().contentType("text/html;charset=UTF-8"));
  }

  @Test
  @WithUserDetails("testUserForPreferences")
  void updateUserPreferencesAndExpectSuccess(@Autowired UserRepo userRepo) throws Exception {
    MyUser testUser = userRepo.findByUsername("testUserForPreferences").orElseThrow();
    assertEquals(DifficultyEnum.ALL, testUser.getUserPreferences().getDifficultyEnum());
    String data =
        "id=4&added=2024-12-03T08%3A12%3A56.562232Z&apiChoiceEnum=ALL&difficultyEnum=HARD&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&_categoryTriviaApiList=on&categoryTriviaApiList=ARTS_AND_LITERATURE&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&_categoryOpenTdbList=on&categoryOpenTdbList=SCIENCE_NATURE";
    mockMvc
        .perform(
            post("/userPreferences")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(data)
                .with(csrf()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
    testUser = userRepo.findByUsername("testUserForPreferences").orElseThrow();
    assertEquals(ApiChoiceEnum.ALL, testUser.getUserPreferences().getApiChoiceEnum());
    assertEquals(DifficultyEnum.HARD, testUser.getUserPreferences().getDifficultyEnum());
    assertTrue(
        testUser
            .getUserPreferences()
            .getCategoryTriviaApiList()
            .contains(CategoryTriviaApi.ARTS_AND_LITERATURE));
    assertFalse(
        testUser
            .getUserPreferences()
            .getCategoryTriviaApiList()
            .contains(CategoryTriviaApi.HISTORY));
    assertTrue(
        testUser
            .getUserPreferences()
            .getCategoryOpenTdbList()
            .contains(CategoryOpenTdb.SCIENCE_NATURE));
    assertFalse(
        testUser
            .getUserPreferences()
            .getCategoryOpenTdbList()
            .contains(CategoryOpenTdb.ENTERTAINMENT_FILM));
  }

  @Test
  void deleteCurrentUserAndExpectUnauthorized(@Autowired UserRepo userRepo) throws Exception {
    mockMvc
        .perform(post("/deleteAccount").with(csrf()))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(forwardedUrl("/error401"));
  }

  @Test
  @WithUserDetails("testUserForDelete")
  void deleteCurrentUserAndExpectSuccess(@Autowired UserRepo userRepo) throws Exception {
    assertNotNull(userRepo.findByUsername("testUserForDelete").orElseThrow());
    mockMvc
        .perform(post("/deleteAccount").with(csrf()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
    assertNull(userRepo.findByUsername("testUserForDelete").orElse(null));
  }

  @Test
  void populateOpenTdbCategories(@Autowired UserController userController) {
    System.out.println(userController.populateOpenTdbCategories());
    assertEquals(
        List.of(
            CategoryOpenTdb.GENERAL_KNOWLEDGE,
            CategoryOpenTdb.ENTERTAINMENT_BOOKS,
            CategoryOpenTdb.ENTERTAINMENT_FILM,
            CategoryOpenTdb.ENTERTAINMENT_MUSIC,
            CategoryOpenTdb.ENTERTAINMENT_MUSICALS_THEATRES,
            CategoryOpenTdb.ENTERTAINMENT_TELEVISION,
            CategoryOpenTdb.ENTERTAINMENT_VIDEO_GAMES,
            CategoryOpenTdb.ENTERTAINMENT_BOARD_GAMES,
            CategoryOpenTdb.ENTERTAINMENT_JAPANESE_ANIME_MANGA,
            CategoryOpenTdb.ENTERTAINMENT_CARTOON_ANIMATIONS,
            CategoryOpenTdb.ENTERTAINMENT_COMICS,
            CategoryOpenTdb.SCIENCE_NATURE,
            CategoryOpenTdb.SCIENCE_COMPUTERS,
            CategoryOpenTdb.SCIENCE_MATHEMATICS,
            CategoryOpenTdb.SCIENCE_GADGETS,
            CategoryOpenTdb.MYTHOLOGY,
            CategoryOpenTdb.SPORTS,
            CategoryOpenTdb.GEOGRAPHY,
            CategoryOpenTdb.HISTORY,
            CategoryOpenTdb.POLITICS,
            CategoryOpenTdb.ART,
            CategoryOpenTdb.CELEBRITIES,
            CategoryOpenTdb.ANIMALS,
            CategoryOpenTdb.VEHICLES),
        userController.populateOpenTdbCategories());
  }

  @Test
  void populateTriviaApiCategories(@Autowired UserController userController) {
    System.out.println(userController.populateTriviaApiCategories());
    assertEquals(
        List.of(
            CategoryTriviaApi.MUSIC,
            CategoryTriviaApi.SPORT_AND_LEISURE,
            CategoryTriviaApi.FILM_AND_TV,
            CategoryTriviaApi.ARTS_AND_LITERATURE,
            CategoryTriviaApi.HISTORY,
            CategoryTriviaApi.SOCIETY_AND_CULTURE,
            CategoryTriviaApi.SCIENCE,
            CategoryTriviaApi.GEOGRAPHY,
            CategoryTriviaApi.FOOD_AND_DRINK,
            CategoryTriviaApi.GENERAL_KNOWLEDGE),
        userController.populateTriviaApiCategories());
  }
}
