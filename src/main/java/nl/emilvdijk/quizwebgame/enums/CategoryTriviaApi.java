package nl.emilvdijk.quizwebgame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * enum class to indicate the category of the quiz questions. chosen by the user and kept in the
 * UserPreferences class.
 *
 * @see nl.emilvdijk.quizwebgame.entity.UserPreferences
 * @author Emil van Dijk
 */
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public enum CategoryTriviaApi {
  MUSIC("music"),
  SPORT_AND_LEISURE("sport_and_leisure"),
  FILM_AND_TV("film_and_tv"),
  ARTS_AND_LITERATURE("arts_and_literature"),
  HISTORY("history"),
  SOCIETY_AND_CULTURE("society_and_culture"),
  SCIENCE("science"),
  GEOGRAPHY("geography"),
  FOOD_AND_DRINK("food_and_drink"),
  GENERAL_KNOWLEDGE("general_knowledge");

  public static final CategoryTriviaApi[] ALL = {
    MUSIC,
    SPORT_AND_LEISURE,
    FILM_AND_TV,
    ARTS_AND_LITERATURE,
    HISTORY,
    SOCIETY_AND_CULTURE,
    SCIENCE,
    GEOGRAPHY,
    FOOD_AND_DRINK,
    GENERAL_KNOWLEDGE
  };
  private final String displayValue;
}
