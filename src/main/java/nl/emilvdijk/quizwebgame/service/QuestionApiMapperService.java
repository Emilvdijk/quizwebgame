package nl.emilvdijk.quizwebgame.service;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.springframework.stereotype.Service;

@Service
public class QuestionApiMapperService {

  public List<Question> mapQuestions(List<QuestionTriviaApi> questionTriviaApis) {
    List<Question> questionList = new ArrayList<>();
    for (QuestionTriviaApi questionTriviaApi : questionTriviaApis) {
      Question question = new Question();
      question.setQuestionText(questionTriviaApi.getQuestion().get("text"));
      question.setCorrectAnswer(questionTriviaApi.getCorrectAnswer());
      question.setIncorrectAnswers(questionTriviaApi.getIncorrectAnswers());
      // FIXME combine similar categories
      question.setCategory(questionTriviaApi.getCategory());
      question.setTags(questionTriviaApi.getTags());
      question.setType(questionTriviaApi.getType());
      question.setDifficulty(questionTriviaApi.getDifficulty());
      question.setOrigin(ApiChoiceEnum.TRIVIAAPI);
      questionList.add(question);
    }
    return questionList;
  }
}
