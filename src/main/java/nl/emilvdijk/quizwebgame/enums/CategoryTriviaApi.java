package nl.emilvdijk.quizwebgame.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
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

  CategoryTriviaApi(String displayValue) {
    this.displayValue = displayValue;
  }
}
