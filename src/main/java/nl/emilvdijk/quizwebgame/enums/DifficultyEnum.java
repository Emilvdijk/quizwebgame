package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(force = true)
@ToString
public enum DifficultyEnum {
  EASY("easy"),
  MEDIUM("medium"),
  HARD("hard"),
  ALL("all");

  private final String displayValue;

  DifficultyEnum(String displayValue) {
    this.displayValue = displayValue;
  }
}
