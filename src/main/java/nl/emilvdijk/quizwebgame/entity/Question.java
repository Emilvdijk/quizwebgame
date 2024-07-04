package nl.emilvdijk.quizwebgame.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

  @Id
  String id;
  String category;
  String correctAnswer;
  String[] incorrectAnswers;
  HashMap<String, String> question;
  String[] tags;
  String type;
  String difficulty;
  String[] regions;
  String isNiche;
//  String[] answers;
  HashMap<Integer, String> answers;

//  public void prepareAnswers() {
//    String[] answers = {Arrays.toString(this.getIncorrectAnswers()), this.getCorrectAnswer()};
//    answers.
//    Collections.shuffle(Arrays.asList(answers));
//    this.setAnswers(answers);
//  }

  public void prepareAnswers() {
    HashMap<Integer, String> answers = new HashMap<>();
    answers.put(1, this.correctAnswer);
    int i = 2;
    for (String incorrectAnswer : this.incorrectAnswers) {
      answers.put(i, incorrectAnswer);
      i++;
      this.answers = answers;
    }
  }
}