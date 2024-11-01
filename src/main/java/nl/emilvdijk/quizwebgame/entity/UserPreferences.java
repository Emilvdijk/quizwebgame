package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPreferences extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -682768038923270772L;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum apiChoiceEnum;

  @Enumerated(EnumType.STRING)
  private DifficultyEnum difficultyEnum;

  public String getDifficultyUriVariables() {
    return switch (difficultyEnum) {
      case EASY -> DifficultyEnum.EASY.getDisplayValue();
      case MEDIUM -> DifficultyEnum.MEDIUM.getDisplayValue();
      case HARD -> DifficultyEnum.HARD.getDisplayValue();
      case ALL -> null;
    };
  }

  public String getCatagoryUriVariables() {
    // FIXME write method
    return null;
  }
}
