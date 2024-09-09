package nl.emilvdijk.quizwebgame.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

  @Autowired UserRepo userRepo;

  /**
   * save the user to the repository
   *
   * @param user to be saved
   */
  public void save(MyUser user) {
    if (userRepo.existsMyUserByUsername(user.getUsername())) {
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
   * adds 2 user profiles for testing. should be removed if moved from testing or development FIXME
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
}
