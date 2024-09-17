package nl.emilvdijk.quizwebgame.config;

import java.util.ArrayList;

import nl.emilvdijk.quizwebgame.service.QuizServiceAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

//  @Autowired
//  QuizServiceAuthenticated quizService;
//
//  @EventListener
//  public void onSuccess(AuthenticationSuccessEvent success) {
//    quizService.setQuestions(new ArrayList<>());
//  }
}
