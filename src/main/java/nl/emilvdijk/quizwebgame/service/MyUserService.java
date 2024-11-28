package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.AnsweredQuestion;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.model.NewMyUser;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * service class for all operations involving users.
 *
 * @author Emil van Dijk
 */
@Service
@Slf4j
@AllArgsConstructor
public class MyUserService implements UserDetailsService {

  @NonNull UserRepo userRepo;
  @NonNull PasswordEncoder passwordEncoder;
  public static final String DEFAULT_USER_ROLE = "ROLE_USER";

  /**
   * save a new user to the repository.
   *
   * @param user new user to be saved
   */
  public void saveUser(MyUser user) {
    if (Boolean.TRUE.equals(checkIfUserExists(user.getUsername()))) {
      log.debug("could not save user because user already exists: {}", user.getUsername());
      return;
    }
    userRepo.save(user);
  }

  /**
   * update the given user in the repository.
   *
   * @param user user to be updated
   */
  public void updateUser(MyUser user) {
    if (Boolean.FALSE.equals(checkIfUserExists(user.getUsername()))) {
      log.error("could not update user because user couldn't be found: {}", user.getUsername());
      return;
    }
    userRepo.save(user);
  }

  /**
   * load user from repository with the given username or if the user is not found a
   * UsernameNotFoundException will be thrown.
   *
   * @param username username of the desired user
   * @return the user if found
   * @throws UsernameNotFoundException if the user is not found
   */
  @Override
  public MyUser loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepo
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
  }

  /**
   * given new user will be registered and saved to the repository.
   *
   * @param newUser user to be saved
   */
  public void registerNewUser(NewMyUser newUser) {
    MyUser registerUser = constructUser(newUser);
    saveUser(registerUser);
    log.debug("new account created with username: {}", registerUser.getUsername());
  }

  /**
   * transforms dto to user. the password will be encoded using the current password encoder. a user
   * role will be given. a user preferences object will be added to the user with the default state.
   *
   * @param newUser to be transformed to user
   * @return new user object
   */
  private MyUser constructUser(NewMyUser newUser) {
    return MyUser.builder()
        .username(newUser.getUsername())
        .password(passwordEncoder.encode(newUser.getPassword()))
        .myRoles(List.of(DEFAULT_USER_ROLE))
        .questions(new ArrayList<>())
        .enabled(true)
        .userPreferences(
            UserPreferences.builder()
                .apiChoiceEnum(ApiChoiceEnum.TRIVIA_API)
                .difficultyEnum(DifficultyEnum.ALL)
                .build())
        .build();
  }

  /**
   * adds a new AnsweredQuestion object to the list of the user and updates the user in the
   * repository.
   *
   * @see AnsweredQuestion
   * @param question the question that has been answered
   * @param user the user that answered the question
   * @param chosenAnswer the answer the user has chosen
   */
  public void markQuestionDone(Question question, MyUser user, String chosenAnswer) {
    MyUser myUser = loadUserByUsername(user.getUsername());
    myUser
        .getAnsweredQuestions()
        .add(
            new AnsweredQuestion(
                question, chosenAnswer, Objects.equals(chosenAnswer, question.getCorrectAnswer())));
    updateUser(myUser);
  }

  /**
   * check if the user with the given username already exists in the repository.
   *
   * @param username username to look for
   * @return boolean of whether the user exists or not
   */
  public Boolean checkIfUserExists(String username) {
    return userRepo.existsMyUserByUsername(username);
  }

  /**
   * deletes the user with the corresponding id from the repository.
   *
   * @param id id of the user to be deleted
   */
  public void deleteUserById(Long id) {
    userRepo.deleteById(id);
  }

  /**
   * resets the given users preferences field to a default state.
   *
   * @param myUser owner of the preferences to be reset
   */
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
    log.debug("preferences reset for user: {}", user.getUsername());
  }
}
