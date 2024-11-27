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
public enum CategoryOpenTDB {
  GENERAL_KNOWLEDGE("General Knowledge"),
  ENTERTAINMENT_BOOKS("Entertainment: Books"),
  ENTERTAINMENT_FILM("Entertainment: Film"),
  ENTERTAINMENT_MUSIC("Entertainment: Music"),
  ENTERTAINMENT_MUSICALS_THEATRES("Entertainment: Musicals & Theatres"),
  ENTERTAINMENT_TELEVISION("Entertainment: Television"),
  ENTERTAINMENT_VIDEO_GAMES("Entertainment: Video Games"),
  ENTERTAINMENT_BOARD_GAMES("Entertainment: Board Games"),
  ENTERTAINMENT_JAPANESE_ANIME_MANGA("Entertainment: Japanese Anime & Manga"),
  ENTERTAINMENT_CARTOON_ANIMATIONS("Entertainment: Cartoon & Animations"),
  ENTERTAINMENT_COMICS("Entertainment: Comics"),
  SCIENCE_NATURE("Science & Nature"),
  SCIENCE_COMPUTERS("Science: Computers"),
  SCIENCE_MATHEMATICS("Science: Mathematics"),
  SCIENCE_GADGETS("Science: Gadgets"),
  MYTHOLOGY("Mythology"),
  SPORTS("Sports"),
  GEOGRAPHY("Geography"),
  HISTORY("History"),
  POLITICS("Politics"),
  ART("Art"),
  CELEBRITIES("Celebrities"),
  ANIMALS("Animals"),
  VEHICLES("Vehicles");

  public static final CategoryOpenTDB[] ALL = {
    GENERAL_KNOWLEDGE,
    ENTERTAINMENT_BOOKS,
    ENTERTAINMENT_FILM,
    ENTERTAINMENT_MUSIC,
    ENTERTAINMENT_MUSICALS_THEATRES,
    ENTERTAINMENT_TELEVISION,
    ENTERTAINMENT_VIDEO_GAMES,
    ENTERTAINMENT_BOARD_GAMES,
    ENTERTAINMENT_JAPANESE_ANIME_MANGA,
    ENTERTAINMENT_CARTOON_ANIMATIONS,
    ENTERTAINMENT_COMICS,
    SCIENCE_NATURE,
    SCIENCE_COMPUTERS,
    SCIENCE_MATHEMATICS,
    SCIENCE_GADGETS,
    MYTHOLOGY,
    SPORTS,
    GEOGRAPHY,
    HISTORY,
    POLITICS,
    ART,
    CELEBRITIES,
    ANIMALS,
    VEHICLES
  };

  private final String displayValue;
}
