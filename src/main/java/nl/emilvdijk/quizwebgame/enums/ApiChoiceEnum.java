package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public enum ApiChoiceEnum {
  TRIVIAAPI("Trivia Api"),
  OPENTDB("OpenTDB"),
  ALL("All");

  private final String displayValue;

  ApiChoiceEnum(String displayValue) {
    this.displayValue = displayValue;
  }
}
