package nl.emilvdijk.quizwebgame.repository;

import java.util.List;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository class for Question class. implements several derived query generated methods.
 * implements JpaSpecificationExecutor to query by specification. several specification methods have
 * been written in the Question class.
 *
 * @see Question
 * @author Emil van Dijk
 */
@Repository
public interface QuestionRepo
    extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

  List<Question> findByIdNotIn(List<Long> idList);

  List<Question> findByIdNotInAndOrigin(List<Long> myIdList, ApiChoiceEnum origin);
}
