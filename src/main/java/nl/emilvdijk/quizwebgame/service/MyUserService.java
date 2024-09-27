package nl.emilvdijk.quizwebgame.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.dto.MyUserDto;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

  @Autowired UserRepo userRepo;
  @Autowired PasswordEncoder passwordEncoder;

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

  @Override
  public MyUser loadUserByUsername(String username) throws UsernameNotFoundException {
    MyUser user = userRepo.findByUsername(username);
    // FIXME change null check
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return user;
  }

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
            .build();
    saveUser(testUser);

    ArrayList<String> adminRoles = new ArrayList<>();
    adminRoles.add("ROLE_ADMIN");
    adminRoles.add("ROLE_USER");
    MyUser testAdmin =
        MyUser.builder()
            .username("1")
            .password("$2a$10$ixsefZtwnAoLc10H/R6Tu.NBQgWKnhgx5vXs.r2aYp32IjKE6YlCu")
            .myRoles(adminRoles)
            .enabled(true)
            .build();
    saveUser(testAdmin);
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
    return MyUser.builder()
        .username(newUser.getUsername())
        .password(passwordEncoder.encode(newUser.getPassword()))
        .myRoles(userRoles)
        .enabled(true)
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
}
