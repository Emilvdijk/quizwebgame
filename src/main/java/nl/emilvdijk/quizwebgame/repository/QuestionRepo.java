package nl.emilvdijk.quizwebgame.repository;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {

  List<Question> findBymyIdNotIn(List<Long> myIdList);

  Question findBymyId(Long myId);

  List<Question> findBymyIdNotInAndOrigin(List<Long> myIdList, ApiChoiceEnum choiceEnum);
}
