package nl.emilvdijk.quizwebgame.dto;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class QuestionOpentdb {
  private String response_code;
  private List<QuestionOpentdbQuestion> results;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  @Builder(access = AccessLevel.PUBLIC)
  public static class QuestionOpentdbQuestion {
    private String type;
    private String difficulty;
    private String category;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;
  }
}
