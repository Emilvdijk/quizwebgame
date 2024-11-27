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

  Optional<MyUser> findByUsername(String username);

  boolean existsMyUserByUsername(String username);
}
