package nl.emilvdijk.quizwebgame.service.api;

import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb.QuestionOpentdbQuestion;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapstruct generating class used to map OpenTDB quiz questions to Question class objects.
 *
 * @see nl.emilvdijk.quizwebgame.dto.QuestionOpentdb
 * @see Question
 * @author Emil van Dijk
 */
@Mapper
public interface QuestionMapStructMapper {
  QuestionMapStructMapper INSTANCE = Mappers.getMapper(QuestionMapStructMapper.class);

  /**
   * this will generate a method to convert a list of QuestionOpenTdbQuestions to a list of
   * questions.
   *
   * @param questionOpenTdbQuestionList list to convert
   * @return returns a list of questions
   */
  List<Question> questionOpenTdbListToQuestionList(
      List<QuestionOpentdbQuestion> questionOpenTdbQuestionList);

  /**
   * converts a single QuestionOpenTdbQuestion to a question object.
   *
   * @param questionOpenTdbQuestionList object to convert
   * @return returns a question object
   */
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "origin", ignore = true)
  @Mapping(target = "lastUpdatedOn", ignore = true)
  @Mapping(target = "answers", ignore = true)
  @Mapping(target = "added", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "questionText", source = "question")
  @Mapping(target = "correctAnswer", source = "correct_answer")
  @Mapping(target = "incorrectAnswers", source = "incorrect_answers")
  @Mapping(target = "category", source = "category")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "difficulty", source = "difficulty")
  Question questionOpentdbToQuestion(QuestionOpentdbQuestion questionOpenTdbQuestionList);
}
