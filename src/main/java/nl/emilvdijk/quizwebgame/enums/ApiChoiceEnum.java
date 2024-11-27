package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * enum class to indicate the origin of the quiz questions. chosen by the user and kept in the
 * UserPreferences class.
 *
 * @see nl.emilvdijk.quizwebgame.entity.UserPreferences
 * @author Emil van Dijk
 */
@Getter
@NoArgsConstructor(force = true)
public enum ApiChoiceEnum {
  TRIVIA_API("Trivia Api"),
  OPEN_TDB("OpenTDB"),
  ALL("All");

  private final String displayValue;

  ApiChoiceEnum(String displayValue) {
    this.displayValue = displayValue;
  }
}
