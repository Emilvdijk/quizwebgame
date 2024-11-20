package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionApiService {

  QuestionApiMapperService questionApiMapperService;
  ApiUrlBuilder apiUrlBuilder;

  public List<Question> getNewQuestions(MyUser user) {
    return switch (user.getUserPreferences().getApiChoiceEnum()) {
      case TRIVIAAPI -> getNewTriviaApiQuestions(user);
      case OPENTDB -> getNewOpenTdbQuestions(user);
      case ALL -> getNewQuestionsFromAll(user);
    };
  }

  public List<Question> getDefaultQuestions() {
    QuestionsApiCaller<List<QuestionTriviaApi>> questionsApiService =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
    return questionApiMapperService.mapTriviaApiQuestions(
        questionsApiService.getNewQuestions(apiUrlBuilder.getDefault()));
  }

  private List<Question> getNewTriviaApiQuestions(MyUser user) {
    URI uri = apiUrlBuilder.generateTriviaApiUri(user);
    QuestionsApiCaller<List<QuestionTriviaApi>> questionsApiService =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
    return questionApiMapperService.mapTriviaApiQuestions(questionsApiService.getNewQuestions(uri));
  }

  private List<Question> getNewOpenTdbQuestions(MyUser user) {
    URI uri = apiUrlBuilder.generateUriOpenTdb(user);
    QuestionsApiCaller<QuestionOpentdb> questionsApiCaller =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
    QuestionOpentdb questionOpenTdbResponse = questionsApiCaller.getNewQuestions(uri);
    if (Objects.equals(questionOpenTdbResponse.getResponse_code(), "1")) {
      log.error(
          "Could not return results. The OpenTdb API doesn't have enough questions for your query.");
      throw new ApiErrorException(
          "Could not return results. The OpenTdb API doesn't have enough questions for your query.");
    }
    return questionApiMapperService.mapOpenTDBQuestions(questionOpenTdbResponse);
  }

  private List<Question> getNewQuestionsFromAll(MyUser user) {
    List<Question> newQuestionsList = new ArrayList<>();
    newQuestionsList.addAll(getNewTriviaApiQuestions(user));
    newQuestionsList.addAll(getNewOpenTdbQuestions(user));
    return newQuestionsList;
  }
}
