package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Question implements Serializable {

  @Serial private static final long serialVersionUID = -4638285421950167006L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long myId;

  private String questionText;
  private String correctAnswer;
  private List<String> incorrectAnswers;
  private String category;
  private List<String> tags;
  private String type;
  private String difficulty;
  @Transient private List<String> answers;

  @CreationTimestamp private Instant added;
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
