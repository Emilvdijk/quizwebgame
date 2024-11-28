package nl.emilvdijk.quizwebgame.service;

import nl.emilvdijk.quizwebgame.entity.Question;

/**
 * all operations the quiz service needs to be able to complete.
 *
 * @author Emil van Dijk
 */
public interface QuizService {

  /**
   * returns the applicable role used by the dynamic quiz service.
   *
   * @see DynamicQuizService
   * @return the role this service is applicable to
   */
  String getApplicableRole();

  /**
   * retrieve a new question.
   *
   * @return a new question
   */
  Question getNewQuestion();

  /** gets new questions from the question api and saves them to the repo. */
  void addNewQuestionsFromApi();

  /** once a question has been answered remove it from the temporary list. */
  void removeAnsweredQuestion();

  /**
   * returns the question from the repository with the desired id.
   *
   * @param id the id of the desired question
   * @return the question with the corresponding id
   */
  Question getQuestionById(Long id);
}
