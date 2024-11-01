package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class QuestionApiService {

  QuestionApiMapperService questionApiMapperService;

  private static final String QUIZ_API_URL = "https://the-trivia-api.com/v2/questions?limit=50";
  private static final String QUIZ_API_URL2 = "https://opentdb.com/api.php?amount=50";

  public QuestionApiService(@Autowired QuestionApiMapperService questionApiMapperService) {
    this.questionApiMapperService = questionApiMapperService;
  }

  public List<Question> getNewQuestions(MyUser user) {
    return switch (user.getUserPreferences().getApiChoiceEnum()) {
      case TRIVIAAPI -> getNewTriviaApiQuestions(user);
      case OPENTDB -> getNewOpenTDBQuestions(user);
      case ALL -> getNewQuestionsFromAll(user);
    };
  }

  private List<Question> getNewTriviaApiQuestions(MyUser user) {
    URI uri = generateTriviaApiURI(user);
    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    return questionApiMapperService.mapTriviaApiQuestions(questionsApiService.getNewQuestions(uri));
  }

  private List<Question> getNewOpenTDBQuestions(MyUser user) {
    URI uri = generateURIOpenTDB(user);
    QuestionsApiCaller<QuestionOpentdb> questionsApiCaller =
        new QuestionsApiCaller(new ParameterizedTypeReference<QuestionOpentdb>() {});
    return questionApiMapperService.mapOpenTDBQuestions(questionsApiCaller.getNewQuestion(uri));
  }

  private List<Question> getNewQuestionsFromAll(MyUser user) {
    List<Question> newQuestionsList = new ArrayList<>();
    newQuestionsList.addAll(getNewTriviaApiQuestions(user));
    newQuestionsList.addAll(getNewOpenTDBQuestions(user));
    return newQuestionsList;
  }

  public List<Question> getDefaultQuestions() {
    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    return questionApiMapperService.mapTriviaApiQuestions(
        questionsApiService.getDefaultQuestions());
  }

  private URI generateTriviaApiURI(MyUser user) {
    URI uri =
        UriComponentsBuilder.fromUriString(QUIZ_API_URL)
            .queryParamIfPresent(
                "difficulties",
                Optional.ofNullable(user.getUserPreferences().getDifficultyUriVariables()))
            .queryParamIfPresent(
                "categories",
                Optional.ofNullable(user.getUserPreferences().getCatagoryUriVariables()))
            .build()
            .toUri();
    log.debug("TriviaApi URI generated: {} for user {}", uri, user.getUsername());
    return uri;
  }

  private URI generateURIOpenTDB(MyUser user) {
    URI uri =
        UriComponentsBuilder.fromUriString(QUIZ_API_URL2)
            .queryParamIfPresent(
                "difficulty",
                Optional.ofNullable(user.getUserPreferences().getDifficultyUriVariables()))
            .queryParamIfPresent(
                "category",
                Optional.ofNullable(user.getUserPreferences().getCatagoryUriVariables()))
            .build()
            .toUri();
    log.debug("OpenTDB URI generated: {} for user {}", uri, user.getUsername());
    return uri;
  }
}
