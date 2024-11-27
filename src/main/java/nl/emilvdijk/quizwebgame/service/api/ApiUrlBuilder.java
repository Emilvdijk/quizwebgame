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

  URI getDefault() {
    return UriComponentsBuilder.fromUriString(triviaApiUrl).build().toUri();
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
