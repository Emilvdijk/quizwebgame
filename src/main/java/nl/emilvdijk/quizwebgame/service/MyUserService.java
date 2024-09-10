package nl.emilvdijk.quizwebgame.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.MyUserDto;
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
   * save the user to the repository
   *
   * @param user to be saved
   */
  public void save(MyUser user) {
    if (checkIfUserExists(user.getUsername())) {
      //      FIXME make proper exception when username already exists
      return;
    }
    userRepo.save(user);
  }

  @Override
  public MyUser loadUserByUsername(String username) throws UsernameNotFoundException {
    MyUser user = userRepo.findByUsername(username);
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
    System.out.println("hello world, I have just started up");

    ArrayList<String> userroles = new ArrayList<>();
    userroles.add("ROLE_USER");
    MyUser testsaveuser =
        MyUser.builder()
            .username("user")
            .password("$2a$10$pJ/ahJVBfkGOjzgyOwZWselKRv6WcsaGFc8Tf1A0VkeUFhpX2jEMG")
            .myRoles(userroles)
            .enabled(true)
            .build();
    save(testsaveuser);

    ArrayList<String> adminroles = new ArrayList<>();
    adminroles.add("ROLE_ADMIN");
    MyUser testsaveadmin =
        MyUser.builder()
            .username("1")
            .password("$2a$10$ixsefZtwnAoLc10H/R6Tu.NBQgWKnhgx5vXs.r2aYp32IjKE6YlCu")
            .myRoles(adminroles)
            .enabled(true)
            .build();
    save(testsaveadmin);
  }

  /**
   * transform new user dto to a user object and save it to repo
   * @param newUser user dto to be saved
   */
  public void registerNewUser(MyUserDto newUser) {
    MyUser regesterUser = constructUser(newUser);
    save(regesterUser);
  }

  /**
   * transforms dto to user
   * @param newUser to be transformed to user
   * @return new user object
   */
  private MyUser constructUser(MyUserDto newUser) {
    ArrayList<String> userroles = new ArrayList<>();
    userroles.add("ROLE_USER");
    return MyUser.builder()
        .username(newUser.getUsername())
        .password(passwordEncoder.encode(newUser.getPassword()))
        .myRoles(userroles)
        .enabled(true)
        .build();
  }

  public Boolean checkIfUserExists(String username){
    return userRepo.existsMyUserByUsername(username);  }
}
