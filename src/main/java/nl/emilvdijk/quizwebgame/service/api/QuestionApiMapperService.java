package nl.emilvdijk.quizwebgame.service.api;

import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * Mapper class to map the api supplier questions to Question class objects.
 *
 * @see Question
 * @author Emil van Dijk
 */
@Service
public class QuestionApiMapperService {

  public List<Question> mapTriviaApiQuestions(List<QuestionTriviaApi> questionTriviaApis) {
    List<Question> questionList = new ArrayList<>();
    for (QuestionTriviaApi questionTriviaApi : questionTriviaApis) {
      Question question = new Question();
      question.setQuestionText(questionTriviaApi.getQuestion().get("text"));
      question.setCorrectAnswer(questionTriviaApi.getCorrectAnswer());
      question.setIncorrectAnswers(questionTriviaApi.getIncorrectAnswers());
      question.setCategory(questionTriviaApi.getCategory());
      question.setTags(questionTriviaApi.getTags());
      question.setType(questionTriviaApi.getType());
      question.setDifficulty(questionTriviaApi.getDifficulty());
      question.setOrigin(ApiChoiceEnum.TRIVIA_API);
      questionList.add(question);
    }
    return questionList;
  }

  public List<Question> mapOpenTDBQuestions(QuestionOpentdb newQuestions) {
    List<Question> questionList =
        QuestionMapStructMapper.INSTANCE.questionOpenTdbListToQuestionList(
            newQuestions.getResults());
    questionList.forEach(question -> question.setOrigin(ApiChoiceEnum.OPEN_TDB));
    questionList.forEach(
        question -> question.setQuestionText(HtmlUtils.htmlUnescape(question.getQuestionText())));
    questionList.forEach(
        question -> question.setCorrectAnswer(HtmlUtils.htmlUnescape(question.getCorrectAnswer())));
    questionList.forEach(
        question ->
            question.setIncorrectAnswers(
                question.getIncorrectAnswers().stream().map(HtmlUtils::htmlUnescape).toList()));
    return questionList;
  }
}
