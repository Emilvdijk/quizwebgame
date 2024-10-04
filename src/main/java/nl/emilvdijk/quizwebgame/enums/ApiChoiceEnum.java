package nl.emilvdijk.quizwebgame.enums;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public enum ApiChoiceEnum {
  TRIVIAAPI,
  OPENTDB;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
}
