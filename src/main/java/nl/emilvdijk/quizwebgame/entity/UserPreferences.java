package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTDB;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPreferences extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -682768038923270772L;

  @UpdateTimestamp private Instant lastUpdatedOn;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum apiChoiceEnum;

  @Enumerated(EnumType.STRING)
  private DifficultyEnum difficultyEnum;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<CategoryTriviaApi> categoryTriviaApi = new ArrayList<>();

  @ElementCollection(fetch = FetchType.EAGER)
  private List<CategoryOpenTDB> categoryOpenTDBS = new ArrayList<>();
}
