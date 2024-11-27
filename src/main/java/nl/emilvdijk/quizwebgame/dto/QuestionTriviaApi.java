package nl.emilvdijk.quizwebgame.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * dta class used in QuestionApiService class. field names are the same as the names given by the
 * external api for ease of use and simplicity. this is to be converted to a Question class object
 * using the QuestionApiMapperService.
 *
 * @see nl.emilvdijk.quizwebgame.service.api.QuestionApiService
 * @see nl.emilvdijk.quizwebgame.entity.Question
 * @see nl.emilvdijk.quizwebgame.service.api.QuestionApiMapperService
 * @author Emil van Dijk
 */
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
