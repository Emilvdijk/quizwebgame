package nl.emilvdijk.quizwebgame.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.dto.MyUserDto;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserService implements UserDetailsService {

  UserRepo userRepo;
  PasswordEncoder passwordEncoder;

  public MyUserService(@Autowired PasswordEncoder passwordEncoder, @Autowired UserRepo userRepo) {
    this.passwordEncoder = passwordEncoder;
    this.userRepo = userRepo;
  }

  /**
   * save the user to the repository.
   *
   * @param user to be saved
   */
  public void saveUser(MyUser user) {
    if (checkIfUserExists(user.getUsername())) {
      //      FIXME make proper exception when username already exists
      return;
    }
    userRepo.save(user);
  }

  public void updateUser(MyUser user) {
    if (!checkIfUserExists(user.getUsername())) {
      //      FIXME make proper exception when user doesnt exists
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
   * adds 2 user profiles for testing. FIXME should be removed if moved from testing or development
   * remove after testing or development
   */
  @PostConstruct
  public void addTestUsersAfterStartup() {
    ArrayList<String> userRoles = new ArrayList<>();
    userRoles.add("ROLE_USER");
    UserPreferences userPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
            .quizApiUriVariables(new HashMap<>())
            .build();
    MyUser testUser =
        MyUser.builder()
            .username("user")
            .password("$2a$10$pJ/ahJVBfkGOjzgyOwZWselKRv6WcsaGFc8Tf1A0VkeUFhpX2jEMG")
            .myRoles(userRoles)
            .enabled(true)
            .userPreferences(userPreferences)
            .build();
    saveUser(testUser);

    ArrayList<String> adminRoles = new ArrayList<>();
    adminRoles.add("ROLE_ADMIN");
    adminRoles.add("ROLE_USER");
    UserPreferences adminPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
            .quizApiUriVariables(new HashMap<>())
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
   * transform new user dto to a user object and save it to repo.
   *
   * @param newUser user dto to be saved
   */
  public void registerNewUser(MyUserDto newUser) {
    MyUser registerUser = constructUser(newUser);
    saveUser(registerUser);
  }

  /**
   * transforms dto to user.
   *
   * @param newUser to be transformed to user
   * @return new user object
   */
  private MyUser constructUser(MyUserDto newUser) {
    ArrayList<String> userRoles = new ArrayList<>();
    userRoles.add("ROLE_USER");
    UserPreferences userPreferences =
        UserPreferences.builder()
            .apiChoiceEnum(ApiChoiceEnum.TRIVIAAPI)
            .quizApiUriVariables(new HashMap<>())
            .build();
    return MyUser.builder()
        .username(newUser.getUsername())
        .password(passwordEncoder.encode(newUser.getPassword()))
        .myRoles(userRoles)
        .enabled(true)
        .userPreferences(userPreferences)
        .build();
  }

  /**
   * adds a link of the question and the current user to the database. the link will signify that
   * the question is already answered by the user
   *
   * @param question question to be linked to user
   * @param user user to be linked to question
   */
  public void markQuestionDone(Question question, MyUser user) {
    MyUser myUser = loadUserByUsername(user.getUsername());
    myUser.getAnsweredQuestions().add(question);
    userRepo.save(myUser);
  }

  public Boolean checkIfUserExists(String username) {
    return userRepo.existsMyUserByUsername(username);
  }

  public void deleteUserById(Long id) {
    userRepo.deleteById(id);
  }
}
