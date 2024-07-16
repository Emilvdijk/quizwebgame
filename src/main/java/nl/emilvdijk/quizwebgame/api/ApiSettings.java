package nl.emilvdijk.quizwebgame.api;

import java.util.HashMap;
import java.util.Map;

public class ApiSettings {
  //  docs for api:https://the-trivia-api.com/docs/v2/#tag/Questions
  static final String QUIZ_API_URL = "https://the-trivia-api.com/v2/questions";
  static Map<String, String> quizApiUriVariables = new HashMap<>();

  //  other option
  static final String QUIZ_API_URL2 = "https://opentdb.com/api.php?amount=10";

  public static void setQuizApiUriVariables(Map<String, String> quizApiUriVariables) {
    ApiSettings.quizApiUriVariables = quizApiUriVariables;
  }
}
