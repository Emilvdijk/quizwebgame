package nl.emilvdijk.quizwebgame.enums;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(force = true)
public enum ApiChoiceEnum {
  TRIVIAAPI("Trivia Api"),
  OPENTDB("OpenTDB"),
  ALL("All");

  private final String displayValue;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  ApiChoiceEnum(String displayValue) {
    this.displayValue = displayValue;
  }
}
