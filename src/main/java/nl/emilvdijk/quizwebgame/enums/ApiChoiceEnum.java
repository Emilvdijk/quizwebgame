package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
