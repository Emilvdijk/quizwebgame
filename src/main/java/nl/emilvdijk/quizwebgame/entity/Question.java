package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
  private Long myid;

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

  @ManyToMany(mappedBy = "answeredQuestions", fetch = FetchType.EAGER)
  private List<MyUser> user;

  private LocalDateTime added = LocalDateTime.now();

  /** prepares and populates the answers field */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }

  @Override
  public String toString() {
    return "Question{"
        + "myid="
        + myid
        + ", id='"
        + id
        + '\''
        + ", question="
        + question
        + ", category='"
        + category
        + '\''
        + ", correctAnswer='"
        + correctAnswer
        + '\''
        + ", incorrectAnswers="
        + incorrectAnswers
        + ", tags="
        + tags
        + ", type='"
        + type
        + '\''
        + ", difficulty='"
        + difficulty
        + '\''
        + ", regions="
        + regions
        + ", isNiche='"
        + isNiche
        + '\''
        + ", added="
        + added
        + '}';
  }
}
