package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class Question extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = -4638285421950167006L;

  private String questionText;
  private String correctAnswer;
  private List<String> incorrectAnswers;
  private String category;
  private List<String> tags;
  private String type;
  private String difficulty;
  @Transient private List<String> answers;

  @Enumerated(EnumType.STRING)
  private ApiChoiceEnum origin;

  /** prepares and populates the answers field. */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }
}
