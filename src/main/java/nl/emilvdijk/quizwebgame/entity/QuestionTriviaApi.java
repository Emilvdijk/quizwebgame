package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.time.Instant;
import java.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.emilvdijk.quizwebgame.dto.QuestionDto;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "Question")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class QuestionTriviaApi implements Question {

  @Serial private static final long serialVersionUID = -9154072220747380878L;


  //FIXME add custom sequence gen so it ids other question types as well
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
  @Transient
  private List<String> answers;

  @CreationTimestamp
  private Instant added;

  @Override
  public QuestionDto getQuestionDto() {
    QuestionDto questionDto = new QuestionDto();
    questionDto.setQuestion(this.question.get("text"));
    questionDto.setMyId(this.myId);
    questionDto.setAnswers(this.answers);
    return questionDto;
  }

  /** prepares and populates the answers field. */
  @Override
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }
}
