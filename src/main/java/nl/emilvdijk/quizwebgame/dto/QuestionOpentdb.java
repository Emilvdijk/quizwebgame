package nl.emilvdijk.quizwebgame.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class QuestionOpentdb {
  private String type;
  private String difficulty;
  private String category;
  private String question;
  private String correct_answer;
  private List<String> incorrect_answers;
}
