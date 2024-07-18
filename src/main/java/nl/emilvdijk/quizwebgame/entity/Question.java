package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "questions")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGen")
  @SequenceGenerator(
      name = "question_sequence",
      sequenceName = "question_sequence",
      allocationSize = 1)
  Long myid;

  String id;

  @Setter
  @Column(length = 400)
  HashMap<String, String> question;

  String category;
  String correctAnswer;
  List<String> incorrectAnswers;
  List<String> tags;
  String type;
  String difficulty;
  List<String> regions;
  String isNiche;
  List<String> answers;

  /** prepares and populates the answers field */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(this.incorrectAnswers);
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }
}
