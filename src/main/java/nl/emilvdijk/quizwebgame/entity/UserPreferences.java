package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTdb;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * preferences for users. kept in userPreferences field in MyUser class.
 *
 * @see MyUser
 * @author Emil van Dijk
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@Data
@NoArgsConstructor
public class UserPreferences extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -682768038923270772L;

  @UpdateTimestamp private Instant lastUpdatedOn;

  @NonNull
  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum apiChoiceEnum;

  @NonNull
  @Enumerated(EnumType.STRING)
  private DifficultyEnum difficultyEnum;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<CategoryTriviaApi> categoryTriviaApiList = new ArrayList<>();

  @ElementCollection(fetch = FetchType.EAGER)
  private List<CategoryOpenTdb> categoryOpenTdbList = new ArrayList<>();
}
