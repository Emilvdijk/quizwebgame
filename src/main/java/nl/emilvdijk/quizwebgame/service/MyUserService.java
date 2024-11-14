package nl.emilvdijk.quizwebgame.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.AnsweredQuestion;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.model.MyUserDto;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MyUserService implements UserDetailsService {

  UserRepo userRepo;
  PasswordEncoder passwordEncoder;

  /**
   * adds 2 user profiles for testing. FIXME should be removed if moved from testing or development
   * remove after testing or development
   */
  @PostConstruct
  public void addTestUsersAfterStartup() {
    ArrayList<String> userRoles = new ArrayList<>();
    userRoles.add("ROLE_USER");
    MyUser testUser =
        MyUser.builder()
            .username("user")
            .password("$2a$10$pJ/ahJVBfkGOjzgyOwZWselKRv6WcsaGFc8Tf1A0VkeUFhpX2jEMG")
            .myRoles(userRoles)
            .enabled(true)
            .userPreferences(
                UserPreferences.builder()
                    .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                    .difficultyEnum(DifficultyEnum.ALL)
                    .build())
            .build();
    saveUser(testUser);

    ArrayList<String> adminRoles = new ArrayList<>();
    adminRoles.add("ROLE_ADMIN");
    adminRoles.add("ROLE_USER");
    UserPreferences adminPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
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
    saveUser(testAdmin);
    log.debug("added 2 users");
  }

  /**
   * save the user to the repository.
   *
   * @param user to be saved
   */
  public void saveUser(MyUser user) {
    if (checkIfUserExists(user.getUsername())) {
      log.debug("could not save user because user already exists: {}", user.getUsername());
      return;
    }
    userRepo.save(user);
  }

  public void updateUser(MyUser user) {
    if (!checkIfUserExists(user.getUsername())) {
      log.error("could not update user because user couldnt be found: {}", user.getUsername());
      return;
    }
    userRepo.save(user);
  }

  @Override
  public MyUser loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepo
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
  }

  /**
   * transform new user dto to a user object and save it to repo.
   *
   * @param newUser user dto to be saved
   */
  public void registerNewUser(MyUserDto newUser) {
    MyUser registerUser = constructUser(newUser);
    saveUser(registerUser);
    log.debug("new account created with username: {}", registerUser.getUsername());
  }

  /**
   * transforms dto to user.
   *
   * @param newUser to be transformed to user
   * @return new user object
   */
  private MyUser constructUser(MyUserDto newUser) {
    return MyUser.builder()
        .username(newUser.getUsername())
        .password(passwordEncoder.encode(newUser.getPassword()))
        .myRoles(List.of("ROLE_USER"))
        .enabled(true)
        .userPreferences(
            UserPreferences.builder()
                .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
                .difficultyEnum(DifficultyEnum.ALL)
                .build())
        .build();
  }

  /**
   * adds a link of the question and the current user to the database. the link will signify that
   * the question is already answered by the user
   *
   * @param question question to be linked to user
   * @param user user to be linked to question
   */
  public void markQuestionDone(Question question, MyUser user, String chosenAnswer) {
    MyUser myUser = loadUserByUsername(user.getUsername());
    myUser
        .getAnsweredQuestions()
        .add(
            new AnsweredQuestion(
                user.getId(),
                question.getId(),
                chosenAnswer,
                Objects.equals(chosenAnswer, question.getCorrectAnswer())));
    updateUser(myUser);
  }

  public Boolean checkIfUserExists(String username) {
    return userRepo.existsMyUserByUsername(username);
  }

  public void deleteUserById(Long id) {
    userRepo.deleteById(id);
  }

  public void resetUserSettings(MyUser myUser) {
    UserPreferences userPreferences = myUser.getUserPreferences();
    userPreferences.setApiChoiceEnum(ApiChoiceEnum.ALL);
    userPreferences.setDifficultyEnum(DifficultyEnum.ALL);
    userPreferences.setCategoryTriviaApi(new ArrayList<>());
    userPreferences.setCategoryOpenTDBS(new ArrayList<>());
    myUser.setUserPreferences(userPreferences);
    MyUser user = loadUserByUsername(myUser.getUsername());
    user.setUserPreferences(userPreferences);
    updateUser(user);
  }
}
