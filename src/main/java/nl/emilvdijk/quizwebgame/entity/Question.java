package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Predicate;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTDB;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class Question extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -4638285421950167006L;

  private String questionText;
  private String correctAnswer;
  private List<String> incorrectAnswers;
  private String category;
  private List<String> tags;
  private String type;
  private String difficulty;
  @Transient private List<String> answers;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum origin;

  /** prepares and populates the answers field. */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }

  public static Specification<Question> difficultyEquals(DifficultyEnum difficultyEnum) {
    return (question, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (difficultyEnum != DifficultyEnum.ALL) {
        predicates.add(
            criteriaBuilder.equal(question.get("difficulty"), difficultyEnum.getDisplayValue()));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<Question> idNotIn(List<Long> idList) {
    return (question, query, criteriaBuilder) -> criteriaBuilder.not(question.get("id").in(idList));
  }

  public static Specification<Question> originEquals(ApiChoiceEnum apiChoiceEnum) {
    return (question, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (apiChoiceEnum != ApiChoiceEnum.ALL) {
        predicates.add(criteriaBuilder.equal(question.get("origin"), apiChoiceEnum));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<Question> isOfCategory(UserPreferences userPreferences) {
    //     FIXME check if it fully works correctly
    return (question, query, criteriaBuilder) -> {
      if (userPreferences.getCategoryOpenTDBS().isEmpty()) {
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
      }
      List<Predicate> predicates = new ArrayList<>();
      for (CategoryOpenTDB categoryOpenTDB : userPreferences.getCategoryOpenTDBS()) {
        predicates.add(
            criteriaBuilder.equal(question.get("category"), categoryOpenTDB.getDisplayValue()));
      }
      for (CategoryTriviaApi categoryTriviaApi : userPreferences.getCategoryTriviaApi()) {
        predicates.add(
            criteriaBuilder.equal(question.get("category"), categoryTriviaApi.getDisplayValue()));
      }
      return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };
  }
}
