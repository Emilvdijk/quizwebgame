package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.UserPreferences;
import nl.emilvdijk.quizwebgame.enums.CategoryTriviaApi;
import nl.emilvdijk.quizwebgame.enums.DifficultyEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Uri builder class to build uris for the question api providers that will include uri variables
 * when necessary.
 *
 * @author Emil van Dijk
 */
@Slf4j
@Service
public class ApiUrlBuilder {

  @Value("${myapp.url}")
  private String triviaApiUrl;

  @Value("${myapp.url2}")
  private String openTdbUrl;

  /**
   * generate an uri for the triviaApi website api with added uri variables based on user choice.
   *
   * @param user user to read choices from
   * @return new uri
   */
  URI generateTriviaApiUri(MyUser user) {
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

  /**
   * generate an uri for the openTdb website api with added uri variables based on user choice.
   *
   * @param user user to read choices from
   * @return new uri
   */
  URI generateUriOpenTdb(MyUser user) {
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

  /**
   * returns the default uri for fetching new questions.
   *
   * @return default uri
   */
  URI getDefault() {
    return UriComponentsBuilder.fromUriString(triviaApiUrl).build().toUri();
  }

  /**
   * returns a difficulty uri variable based on user preferences.
   *
   * @param userPreferences user preferences to read from
   * @return returns uri difficulty variable
   */
  private String getDifficultyUriVariables(UserPreferences userPreferences) {
    return switch (userPreferences.getDifficultyEnum()) {
      case EASY -> DifficultyEnum.EASY.getDisplayValue();
      case MEDIUM -> DifficultyEnum.MEDIUM.getDisplayValue();
      case HARD -> DifficultyEnum.HARD.getDisplayValue();
      case ALL -> null;
    };
  }

  /**
   * generates triviaApi category uri variables based on user preferences.
   *
   * @param userPreferences user preferences to read from
   * @return returns category uri variable
   */
  private String getTriviaApiCategoryUriVariables(UserPreferences userPreferences) {
    if (userPreferences.getCategoryTriviaApiList().isEmpty()) {
      return null;
    }
    return String.join(
        ",",
        userPreferences.getCategoryTriviaApiList().stream()
            .map(CategoryTriviaApi::getDisplayValue)
            .toList());
  }

  /**
   * generates openTdb category uri variables based on user preferences.
   *
   * @param userPreferences user preferences to read from
   * @return returns category uri variable
   */
  private String getOpenTdbCategoryUriVariables(UserPreferences userPreferences) {
    if (userPreferences.getCategoryOpenTdbList().isEmpty()) {
      return null;
    }
    // OpenTdb only accepts one category per request and enumerated as well
    return switch (userPreferences.getCategoryOpenTdbList().getFirst()) {
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
            userPreferences.getCategoryOpenTdbList().getFirst().getDisplayValue());
        yield null;
      }
    };
  }
}
