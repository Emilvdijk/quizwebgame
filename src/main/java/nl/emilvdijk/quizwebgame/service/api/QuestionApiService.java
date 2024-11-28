package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * service class responsible for executing api calls to external apis for quiz questions using the
 * appropriate uri, mapping them through the mapper service, and returning Question class objects.
 *
 * @see QuestionApiMapperService
 * @see ApiUrlBuilder
 * @author Emil van Dijk
 */
@Service
@Slf4j
@AllArgsConstructor
public class QuestionApiService {

  @NonNull QuestionApiMapperService questionApiMapperService;
  @NonNull ApiUrlBuilder apiUrlBuilder;

  /**
   * choose which api to approach by user choice.
   *
   * @param user user object to read choices from
   * @return list of new questions
   */
  public List<Question> getNewQuestions(MyUser user) {
    return switch (user.getUserPreferences().getApiChoiceEnum()) {
      case TRIVIA_API -> getNewTriviaApiQuestions(user);
      case OPEN_TDB -> getNewOpenTdbQuestions(user);
      case ALL -> getNewQuestionsFromAll(user);
    };
  }

  /**
   * default method for anonymous users.
   *
   * @return list of new questions
   */
  public List<Question> getDefaultQuestions() {
    QuestionsApiCaller<List<QuestionTriviaApi>> listQuestionsApiCaller =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
    return questionApiMapperService.mapTriviaApiQuestions(
        listQuestionsApiCaller.getNewQuestions(apiUrlBuilder.getDefault()));
  }

  /**
   * approach triviaApi api for new questions based on user choice.
   *
   * @param user user object to read choices from
   * @return list of new questions
   */
  private List<Question> getNewTriviaApiQuestions(MyUser user) {
    URI uri = apiUrlBuilder.generateTriviaApiUri(user);
    QuestionsApiCaller<List<QuestionTriviaApi>> listQuestionsApiCaller =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
    return questionApiMapperService.mapTriviaApiQuestions(
        listQuestionsApiCaller.getNewQuestions(uri));
  }

  /**
   * approach openTdb api for new questions based on user choice.
   *
   * @param user user object to read choices from
   * @return list of new questions
   */
  private List<Question> getNewOpenTdbQuestions(MyUser user) {
    URI uri = apiUrlBuilder.generateUriOpenTdb(user);
    QuestionsApiCaller<QuestionOpentdb> opentdbQuestionsApiCaller =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<>() {});
    QuestionOpentdb questionOpenTdbResponse = opentdbQuestionsApiCaller.getNewQuestions(uri);
    if (Objects.equals(questionOpenTdbResponse.getResponse_code(), "1")) {
      log.error(
          "Could not return results. The OpenTdb API doesn't have enough questions for your query.");
      throw new ApiErrorException(
          "Could not return results. The OpenTdb API doesn't have enough questions for your query.");
    }
    return questionApiMapperService.mapOpenTdbQuestions(questionOpenTdbResponse);
  }

  /**
   * call both apis for new questions based on user choice.
   *
   * @param user user object to read choices from
   * @return list of new questions
   */
  private List<Question> getNewQuestionsFromAll(MyUser user) {
    List<Question> newQuestionsList = new ArrayList<>();
    newQuestionsList.addAll(getNewTriviaApiQuestions(user));
    newQuestionsList.addAll(getNewOpenTdbQuestions(user));
    return newQuestionsList;
  }
}
