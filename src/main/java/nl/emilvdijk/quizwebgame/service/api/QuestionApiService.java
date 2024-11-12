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
import nl.emilvdijk.quizwebgame.enums.CategoryOpenTDB;
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
  private String QUIZ_API_URL;

  @Value("${myapp.url2}")
  private String QUIZ_API_URL2;

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
                Optional.ofNullable(getDifficultyUriVariables(user.getUserPreferences())))
            .queryParamIfPresent(
                "categories",
                Optional.ofNullable(getTriviaApiCategoryUriVariables(user.getUserPreferences())))
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
                Optional.ofNullable(getDifficultyUriVariables(user.getUserPreferences())))
            .queryParamIfPresent(
                "category",
                Optional.ofNullable(getOpenTDBCategoryUriVariables(user.getUserPreferences())))
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

  private String getOpenTDBCategoryUriVariables(UserPreferences userPreferences) {
    if (userPreferences.getCategoryOpenTDBS().isEmpty()) {
      return null;
    }
    // FIXME opentdb only accepts one category per request and enumerated aswell
    List<CategoryOpenTDB> categories = new ArrayList<>();
    userPreferences
        .getCategoryOpenTDBS()
        .forEach(
            category -> {
              if (List.of(CategoryOpenTDB.ALL).contains(category)) {
                categories.add(category);
              }
            });
    // FIXME make it just string instead of List<String>
    List<String> categoriesStrings =
        switch (categories.getFirst()) {
          case GENERAL_KNOWLEDGE -> List.of("9");
          case ENTERTAINMENT_BOOKS -> List.of("10");
          case ENTERTAINMENT_FILM -> List.of("11");
          case ENTERTAINMENT_MUSIC -> List.of("12");
          case ENTERTAINMENT_MUSICALS_THEATRES -> List.of("13");
          case ENTERTAINMENT_TELEVISION -> List.of("14");
          case ENTERTAINMENT_VIDEO_GAMES -> List.of("15");
          case ENTERTAINMENT_BOARD_GAMES -> List.of("16");
          case SCIENCE_NATURE -> List.of("17");
          case SCIENCE_COMPUTERS -> List.of("18");
          case SCIENCE_MATHEMATICS -> List.of("19");
          case MYTHOLOGY -> List.of("20");
          case SPORTS -> List.of("21");
          case GEOGRAPHY -> List.of("22");
          case HISTORY -> List.of("23");
          case POLITICS -> List.of("24");
          case ART -> List.of("25");
          case CELEBRITIES -> List.of("26");
          case ANIMALS -> List.of("27");
          case VEHICLES -> List.of("28");
          case ENTERTAINMENT_COMICS -> List.of("29");
          case SCIENCE_GADGETS -> List.of("30");
          case ENTERTAINMENT_JAPANESE_ANIME_MANGA -> List.of("31");
          case ENTERTAINMENT_CARTOON_ANIMATIONS -> List.of("32");
          default -> {
            log.error("wrong case found for: {}", categories.getFirst().getDisplayValue());
            yield null;
          }
        };
    return String.join(",", categoriesStrings);
  }
}
