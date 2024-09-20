package nl.emilvdijk.quizwebgame.service;

import nl.emilvdijk.quizwebgame.dto.QuestionDto;
import nl.emilvdijk.quizwebgame.entity.QuestionTriviaApi;

public interface QuizService {

  QuestionDto getNewQuestion();

  void getNewQuestions();

  /** gets new questions from the question api and saves them to the repo. */
  void addNewQuestionsFromApi();

  void removeAnsweredQuestion();
}
