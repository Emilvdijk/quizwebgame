package nl.emilvdijk.quizwebgame.repository;

import nl.emilvdijk.quizwebgame.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<MyUser, Long> {
  // FIXME optional myuser
  // https://codedamn.com/news/java/what-are-optional-parameters-in-java
  MyUser findByUsername(String username);

  boolean existsMyUserByUsername(String username);
}
