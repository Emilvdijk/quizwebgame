package nl.emilvdijk.quizwebgame.service;

import nl.emilvdijk.quizwebgame.api.QuestionsApi;
import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.entity.Question;
import nl.emilvdijk.quizwebgame.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface QuizService {

  Question getNewQuestion();

  default void getNewQuestions() {

  }

  /** gets new questions from the question api and saves them to the repo. */
  default void addNewQuestionsFromApi() {
  }
}
