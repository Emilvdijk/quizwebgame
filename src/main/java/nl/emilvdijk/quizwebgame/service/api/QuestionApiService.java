package nl.emilvdijk.quizwebgame.service.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import nl.emilvdijk.quizwebgame.dto.QuestionOpentdb;
import nl.emilvdijk.quizwebgame.dto.QuestionTriviaApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
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
    URI uri = generateURI(user, QUIZ_API_URL);
    QuestionsApiCaller questionsApiService =
        new QuestionsApiCaller(new ParameterizedTypeReference<List<QuestionTriviaApi>>() {});
    return questionApiMapperService.mapTriviaApiQuestions(questionsApiService.getNewQuestions(uri));
  }

  private List<Question> getNewOpenTDBQuestions(MyUser user) {
    URI uri = generateURI(user, QUIZ_API_URL2);
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
    List<Question> newQuestions =
        questionApiMapperService.mapTriviaApiQuestions(questionsApiService.getDefaultQuestions());
    return newQuestions;
  }

  private URI generateURI(MyUser user, String url) {
    URI uri;
    if (user.getUserPreferences().getQuizApiUriVariables().isEmpty()) {
      uri = URI.create(url);
    } else {

      uri =
          UriComponentsBuilder.fromUriString(url)
              .query("categories={categories}")
              .query("difficulties={difficulties}")
              .buildAndExpand(user.getUserPreferences().getQuizApiUriVariables())
              .toUri();
    }
    return uri;
  }
}
