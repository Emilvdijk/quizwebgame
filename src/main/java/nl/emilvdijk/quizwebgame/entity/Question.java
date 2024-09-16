package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "Question")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Question implements Serializable {

  @Serial private static final long serialVersionUID = -9154072220747380878L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long myId;

  private String id;

  @Column(length = 512)
  private HashMap<String, String> question;

  private String category;
  private String correctAnswer;
  private List<String> incorrectAnswers;
  private List<String> tags;
  private String type;
  private String difficulty;
  private List<String> regions;
  private String isNiche;
  private List<String> answers;

  @CreatedDate private LocalDateTime added;

  /** prepares and populates the answers field. */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }
}
