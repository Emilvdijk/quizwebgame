package nl.emilvdijk.quizwebgame.api;

public final class ApiSettings {
  /**
   * the-trivia-api.com api. docs for api:<a
   * href="https://the-trivia-api.com/docs/v2/#tag/Questions">...</a>
   */
  static final String QUIZ_API_URL = "https://the-trivia-api.com/v2/questions";

  /**
   * opentdb an open trivia data base api. docs for api: <a
   * href="https://opentdb.com/api_config.php">...</a>
   */
  static final String QUIZ_API_URL2 = "https://opentdb.com/api.php?amount=10";

  private ApiSettings() {}
}
