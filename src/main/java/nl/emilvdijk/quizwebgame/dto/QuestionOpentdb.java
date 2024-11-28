package nl.emilvdijk.quizwebgame.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * dta class used in QuestionApiService class. field names are the same as the names given by the
 * external api for ease of use and simplicity.
 *
 * @see nl.emilvdijk.quizwebgame.service.api.QuestionApiService
 * @author Emil van Dijk
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class QuestionOpentdb {
  private String response_code;
  private List<QuestionOpentdbQuestion> results;

  /**
   * nested class for simplicity and ease of use.
   *
   * @author Emil van Dijk
   */
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
