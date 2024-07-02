package nl.emilvdijk.quizwebgame.repository;

import nl.emilvdijk.quizwebgame.core.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {}
