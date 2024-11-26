package nl.emilvdijk.quizwebgame.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class QuestionTriviaApi {

  private String id;
  private Map<String, String> question;
  private String category;
  private String correctAnswer;
  private List<String> incorrectAnswers;
  private List<String> tags;
  private String type;
  private String difficulty;
  private List<String> regions;
  private String isNiche;
}
