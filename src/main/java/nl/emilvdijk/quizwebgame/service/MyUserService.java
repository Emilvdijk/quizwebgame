package nl.emilvdijk.quizwebgame.service;

import lombok.NoArgsConstructor;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class MyUserService extends JdbcUserDetailsManager {

  @Autowired UserRepo userRepo;

  /**
   * save the user to the repository
   *
   * @param user to be saved
   */
  public void save(MyUser user) {
    userRepo.save(user);
  }
}
