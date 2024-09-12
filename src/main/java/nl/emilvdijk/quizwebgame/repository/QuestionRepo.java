package nl.emilvdijk.quizwebgame.repository;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {
  // myuser_id
  List<Question> findByUserNotContaining(MyUser user);
}
