package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApiDto;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.stereotype.Service;

@Service
public class QuestionApiMapperService {

  public List<Question> mapQuestion(List<QuestionTriviaApiDto> questionTriviaApiDtos) {
    List<Question> questionList = new ArrayList<>();
    for (QuestionTriviaApiDto questionTriviaApiDto : questionTriviaApiDtos) {
      Question question = new Question();
      question.setQuestionText(questionTriviaApiDto.getQuestion().get("text"));
      question.setCorrectAnswer(questionTriviaApiDto.getCorrectAnswer());
      question.setIncorrectAnswers(questionTriviaApiDto.getIncorrectAnswers());
      // FIXME combine similar categories
      question.setCategory(questionTriviaApiDto.getCategory());
      question.setTags(questionTriviaApiDto.getTags());
      question.setType(questionTriviaApiDto.getType());
      question.setDifficulty(questionTriviaApiDto.getDifficulty());
      questionList.add(question);
    }
    return questionList;
  }
}
