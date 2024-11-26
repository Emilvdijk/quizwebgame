package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Predicate;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTDB;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.Specification;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
public class Question extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -4638285421950167006L;

  @NonNull private String questionText;
  @NonNull private String correctAnswer;
  @NonNull private List<String> incorrectAnswers;
  @NonNull private String category;
  private List<String> tags;
  private String type;
  @NonNull private String difficulty;
  @Transient private List<String> answers;
  @UpdateTimestamp private Instant lastUpdatedOn;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum origin;

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
    return (question, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      for (CategoryOpenTDB categoryOpenTDB : userPreferences.getCategoryOpenTDBS()) {
        predicates.add(
            criteriaBuilder.equal(question.get("category"), categoryOpenTDB.getDisplayValue()));
      }
      for (CategoryTriviaApi categoryTriviaApi : userPreferences.getCategoryTriviaApi()) {
        predicates.add(
            criteriaBuilder.equal(question.get("category"), categoryTriviaApi.getDisplayValue()));
      }
      if (predicates.isEmpty()) {
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
      }
      return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };
  }

  /** prepares and populates the answers field. */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }
}
