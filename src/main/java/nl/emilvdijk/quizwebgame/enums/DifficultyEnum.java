package nl.emilvdijk.quizwebgame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public enum DifficultyEnum {
  EASY("easy"),
  MEDIUM("medium"),
  HARD("hard"),
  ALL("all difficulties");

  private final String displayValue;
}
