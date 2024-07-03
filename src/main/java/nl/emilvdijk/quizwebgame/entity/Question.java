package nl.emilvdijk.quizwebgame.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
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
  HashMap<String,String> question;
  String[] tags;
  String type;
  String difficulty;
  String[] regions;
  String isNiche;
}