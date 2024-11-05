package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public enum Category {
  ENTERTAINMENT_VIDEO_GAMES("Entertainment: Video Games");

  public static final Category[] ALL = {ENTERTAINMENT_VIDEO_GAMES};

  private final String displayValue;

  Category(String displayValue) {
    this.displayValue = displayValue;
  }
}
