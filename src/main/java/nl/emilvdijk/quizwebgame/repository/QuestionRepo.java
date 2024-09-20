package nl.emilvdijk.quizwebgame.repository;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.QuestionTriviaApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<QuestionTriviaApi, Long> {
//FIXME properly map this question entiry
//  https://stackoverflow.com/questions/12627607/jpa-entity-with-a-interface-attribute-is-it-possible
  @Query("select q from Question q where q.myId not in :myIdList")
  List<Question> findByMyidNotIn(List<Long> myIdList);

  Question findByMyid(Long myid);
}
