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
import java.util.*;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Question question1 = (Question) o;
    return Objects.equals(myid, question1.myid) && Objects.equals(id, question1.id) && Objects.equals(question, question1.question) && Objects.equals(category, question1.category) && Objects.equals(correctAnswer, question1.correctAnswer) && Objects.equals(incorrectAnswers, question1.incorrectAnswers) && Objects.equals(tags, question1.tags) && Objects.equals(type, question1.type) && Objects.equals(difficulty, question1.difficulty) && Objects.equals(regions, question1.regions) && Objects.equals(isNiche, question1.isNiche) && Objects.equals(answers, question1.answers) && Objects.equals(added, question1.added);
  }

  @Override
  public int hashCode() {
    return Objects.hash(myid, id, question, category, correctAnswer, incorrectAnswers, tags, type, difficulty, regions, isNiche, answers, added);
  }

}
