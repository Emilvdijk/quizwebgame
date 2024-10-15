package nl.emilvdijk.quizwebgame.service.api;

import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapStructMapper {
  QuestionMapStructMapper INSTANCE = Mappers.getMapper(QuestionMapStructMapper.class);

  @Mapping(target = "questionText", source = "question")
  List<Question> QuestionOpentdbToQuestion(List<QuestionOpentdb> questionOpentdbList);
}
