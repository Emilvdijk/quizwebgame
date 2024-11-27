package nl.emilvdijk.quizwebgame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * enum class to indicate the difficulty of the quiz questions. chosen by the user and kept in the
 * UserPreferences class.
 *
 * @see nl.emilvdijk.quizwebgame.entity.UserPreferences
 * @author Emil van Dijk
 */
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
