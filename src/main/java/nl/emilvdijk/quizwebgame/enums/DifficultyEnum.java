package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public enum DifficultyEnum {
  EASY("easy"),
  MEDIUM("medium"),
  HARD("hard"),
  ALL("all difficulties");

  private final String displayValue;

  DifficultyEnum(String displayValue) {
    this.displayValue = displayValue;
  }
}
