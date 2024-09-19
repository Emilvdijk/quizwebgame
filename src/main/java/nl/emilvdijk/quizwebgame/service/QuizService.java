package nl.emilvdijk.quizwebgame.service;

import nl.emilvdijk.quizwebgame.entity.Question;

public interface QuizService {

  Question getNewQuestion();

  void getNewQuestions();

  /** gets new questions from the question api and saves them to the repo. */
  void addNewQuestionsFromApi();

  void removeAnsweredQuestion();
}
