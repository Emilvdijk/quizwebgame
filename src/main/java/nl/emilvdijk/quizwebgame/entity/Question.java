package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Question {

  @Id String id;

  @Column(length = 400)
  HashMap<String, String> question;

  String category;
  String correctAnswer;
  String[] incorrectAnswers;
  String[] tags;
  String type;
  String difficulty;
  String[] regions;
  String isNiche;
  List<String> answers;

  /** prepares and populates the answers field */
  public void prepareAnswers() {
    List<String> preparedAnswers = new ArrayList<>();
    preparedAnswers.add(this.correctAnswer);
    preparedAnswers.addAll(List.of(this.incorrectAnswers));
    Collections.shuffle(preparedAnswers);
    this.answers = preparedAnswers;
  }
}
