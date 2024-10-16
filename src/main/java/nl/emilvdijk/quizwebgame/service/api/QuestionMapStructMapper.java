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

  List<Question> QuestionOpentdbListToQuestionList(
      List<QuestionOpentdbQuestion> questionOpentdbQuestionList);

  @Mapping(target = "questionText", source = "question")
  @Mapping(target = "correctAnswer", source = "correct_answer")
  @Mapping(target = "incorrectAnswers", source = "incorrect_answers")
  @Mapping(target = "category", source = "category")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "difficulty", source = "difficulty")
  Question QuestionOpentdbToQuestion(QuestionOpentdbQuestion questionOpentdbQuestionList);
}
