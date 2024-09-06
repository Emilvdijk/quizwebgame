package nl.emilvdijk.quizwebgame.service;

import lombok.NoArgsConstructor;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;

@Service
public class MyUserService implements UserDetailsService {

  @Autowired UserRepo userRepo;

  /**
   * save the user to the repository
   *
   * @param user to be saved
   */
  public void save(MyUser user) {
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
}
