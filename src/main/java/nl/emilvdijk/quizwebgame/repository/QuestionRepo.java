package nl.emilvdijk.quizwebgame.repository;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo
    extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

  List<Question> findByIdNotIn(List<Long> idList);

  List<Question> findByIdNotInAndOrigin(List<Long> myIdList, ApiChoiceEnum origin);

  List<Question> findByOrigin(ApiChoiceEnum origin);
}
