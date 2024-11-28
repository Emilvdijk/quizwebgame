package nl.emilvdijk.quizwebgame.repository;

import java.util.Optional;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * simple JpaRepository. implements several derived query generated methods.
 *
 * @author Emil van Dijk
 */
@Repository
public interface UserRepo extends JpaRepository<MyUser, Long> {

  /**
   * returns the user by username wrapped in Optional.
   *
   * @param username username to look for
   * @return user wrapped in optional
   */
  Optional<MyUser> findByUsername(String username);

  /**
   * if a user with the given username exists in the repository returns true.
   *
   * @param username username to look for
   * @return boolean of whether a user with the given username exists
   */
  boolean existsMyUserByUsername(String username);
}
