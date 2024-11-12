package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import nl.emilvdijk.quizwebgame.exceptions.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class QuestionApiService {

  QuestionApiMapperService questionApiMapperService;

  @Value("${myapp.url}")
  private String triviaApiUrl;

  @Value("${myapp.url2}")
  private String openTdbUrl;

  public QuestionApiService(@Autowired QuestionApiMapperService questionApiMapperService) {
    this.questionApiMapperService = questionApiMapperService;
  }

  public List<Question> getNewQuestions(MyUser user) {
    return switch (user.getUserPreferences().getApiChoiceEnum()) {
      case TRIVIAAPI -> getNewTriviaApiQuestions(user);
      case OPENTDB -> getNewOpenTdbQuestions(user);
      case ALL -> getNewQuestionsFromAll(user);
    };
  }

  private List<Question> getNewTriviaApiQuestions(MyUser user) {
    URI uri = generateTriviaApiUri(user);
    QuestionsApiCaller<QuestionTriviaApi> questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    return questionApiMapperService.mapTriviaApiQuestions(questionsApiService.getNewQuestions(uri));
  }

  private List<Question> getNewOpenTdbQuestions(MyUser user) {
    URI uri = generateUriOpenTdb(user);
    QuestionsApiCaller<QuestionOpentdb> questionsApiCaller =
        new QuestionsApiCaller(new ParameterizedTypeReference<QuestionOpentdb>() {});
    QuestionOpentdb questionOpenTdbResponse = questionsApiCaller.getNewQuestion(uri);
    if (Objects.equals(questionOpenTdbResponse.getResponse_code(), "1")) {
      log.error("Could not return results. The API doesn't have enough questions for your query.");
      throw new ApiErrorException(
          "Could not return results. The API doesn't have enough questions for your query.");
    }
    return questionApiMapperService.mapOpenTDBQuestions(questionOpenTdbResponse);
  }

  private List<Question> getNewQuestionsFromAll(MyUser user) {
    List<Question> newQuestionsList = new ArrayList<>();
    newQuestionsList.addAll(getNewTriviaApiQuestions(user));
    newQuestionsList.addAll(getNewOpenTdbQuestions(user));
    return newQuestionsList;
  }

  public List<Question> getDefaultQuestions() {
    QuestionsApiCaller<?> questionsApiService =
        new QuestionsApiCaller<>(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    return questionApiMapperService.mapTriviaApiQuestions(
        questionsApiService.getDefaultQuestions());
  }

  private URI generateTriviaApiUri(MyUser user) {
    URI uri =
        UriComponentsBuilder.fromUriString(triviaApiUrl)
            .queryParamIfPresent(
                "difficulties",
                Optional.ofNullable(getDifficultyUriVariables(user.getUserPreferences())))
            .queryParamIfPresent(
                "categories",
                Optional.ofNullable(getTriviaApiCategoryUriVariables(user.getUserPreferences())))
            .build()
            .toUri();
    log.debug("TriviaApi URI generated: {} for user {}", uri, user.getUsername());
    return uri;
  }

  private URI generateUriOpenTdb(MyUser user) {
    URI uri =
        UriComponentsBuilder.fromUriString(openTdbUrl)
            .queryParamIfPresent(
                "difficulty",
                Optional.ofNullable(getDifficultyUriVariables(user.getUserPreferences())))
            .queryParamIfPresent(
                "category",
                Optional.ofNullable(getOpenTdbCategoryUriVariables(user.getUserPreferences())))
            .build()
            .toUri();
    log.debug("OpenTDB URI generated: {} for user {}", uri, user.getUsername());
    return uri;
  }

  private String getDifficultyUriVariables(UserPreferences userPreferences) {
    return switch (userPreferences.getDifficultyEnum()) {
      case EASY -> DifficultyEnum.EASY.getDisplayValue();
      case MEDIUM -> DifficultyEnum.MEDIUM.getDisplayValue();
      case HARD -> DifficultyEnum.HARD.getDisplayValue();
      case ALL -> null;
    };
  }

  private String getTriviaApiCategoryUriVariables(UserPreferences userPreferences) {
    if (userPreferences.getCategoryTriviaApi().isEmpty()) {
      return null;
    }
    return String.join(
        ",",
        userPreferences.getCategoryTriviaApi().stream()
            .map(CategoryTriviaApi::getDisplayValue)
            .toList());
  }

  private String getOpenTdbCategoryUriVariables(UserPreferences userPreferences) {
    if (userPreferences.getCategoryOpenTDBS().isEmpty()) {
      return null;
    }
    // OpenTdb only accepts one category per request and enumerated as well
    return switch (userPreferences.getCategoryOpenTDBS().getFirst()) {
      case GENERAL_KNOWLEDGE -> "9";
      case ENTERTAINMENT_BOOKS -> "10";
      case ENTERTAINMENT_FILM -> "11";
      case ENTERTAINMENT_MUSIC -> "12";
      case ENTERTAINMENT_MUSICALS_THEATRES -> "13";
      case ENTERTAINMENT_TELEVISION -> "14";
      case ENTERTAINMENT_VIDEO_GAMES -> "15";
      case ENTERTAINMENT_BOARD_GAMES -> "16";
      case SCIENCE_NATURE -> "17";
      case SCIENCE_COMPUTERS -> "18";
      case SCIENCE_MATHEMATICS -> "19";
      case MYTHOLOGY -> "20";
      case SPORTS -> "21";
      case GEOGRAPHY -> "22";
      case HISTORY -> "23";
      case POLITICS -> "24";
      case ART -> "25";
      case CELEBRITIES -> "26";
      case ANIMALS -> "27";
      case VEHICLES -> "28";
      case ENTERTAINMENT_COMICS -> "29";
      case SCIENCE_GADGETS -> "30";
      case ENTERTAINMENT_JAPANESE_ANIME_MANGA -> "31";
      case ENTERTAINMENT_CARTOON_ANIMATIONS -> "32";
      default -> {
        log.error(
            "wrong case found for: {}",
            userPreferences.getCategoryOpenTDBS().getFirst().getDisplayValue());
        yield null;
      }
    };
  }
}
