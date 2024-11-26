package nl.emilvdijk.quizwebgame.service.api;

import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb.QuestionOpentdbQuestion;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapStructMapper {
  QuestionMapStructMapper INSTANCE = Mappers.getMapper(QuestionMapStructMapper.class);

  List<Question> questionOpenTdbListToQuestionList(
      List<QuestionOpentdbQuestion> questionOpentdbQuestionList);

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
