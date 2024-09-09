package nl.emilvdijk.quizwebgame.repository;

import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<MyUser, Long> {

  MyUser findByUsername(String username);

  boolean existsMyUserByUsername(String Username);
}
