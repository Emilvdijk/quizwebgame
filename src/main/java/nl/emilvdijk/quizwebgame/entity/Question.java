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
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.Specification;

/**
 * basic data question entity. several spring specifications methods are defined
 *
 * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">spring
 *     Specifications documentation</a>
 * @author Emil van Dijk
 */
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

  /**
   * checks if the question is of the right difficulty using spring specifications.
   *
   * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">spring
   *     Specifications documentation</a>
   * @param difficultyEnum enum to read criteria from
   * @return specifications object
   */
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

  /**
   * checks if the question id is not in the exclusion list category using spring specifications.
   *
   * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">spring
   *     Specifications documentation</a>
   * @param idList list of ids to exclude
   * @return specifications object
   */
  public static Specification<Question> idNotIn(List<Long> idList) {
    return (question, query, criteriaBuilder) -> criteriaBuilder.not(question.get("id").in(idList));
  }

  /**
   * checks if the question is of the right origin using spring specifications.
   *
   * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">spring
   *     Specifications documentation</a>
   * @param apiChoiceEnum enum object to read criteria from
   * @return specifications object
   */
  public static Specification<Question> originEquals(ApiChoiceEnum apiChoiceEnum) {
    return (question, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (apiChoiceEnum != ApiChoiceEnum.ALL) {
        predicates.add(criteriaBuilder.equal(question.get("origin"), apiChoiceEnum));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  /**
   * checks if the question is of the right category using spring specifications.
   *
   * @see <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">spring
   *     Specifications documentation</a>
   * @param userPreferences userPreferences object to read criteria from
   * @return specifications object
   */
  public static Specification<Question> isOfCategory(UserPreferences userPreferences) {
    return (question, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      for (CategoryOpenTdb categoryOpenTdb : userPreferences.getCategoryOpenTdbList()) {
        predicates.add(
            criteriaBuilder.equal(question.get("category"), categoryOpenTdb.getDisplayValue()));
      }
      for (CategoryTriviaApi categoryTriviaApi : userPreferences.getCategoryTriviaApiList()) {
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
