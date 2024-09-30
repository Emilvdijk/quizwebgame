package nl.emilvdijk.quizwebgame.repository;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {

  // FIXME try to get the derived query right
  @Query("select q from Question q where q.myId not in :myIdList")
  List<Question> findByMyidNotIn(List<Long> myIdList);

  Question findBymyId(Long myId);
}
