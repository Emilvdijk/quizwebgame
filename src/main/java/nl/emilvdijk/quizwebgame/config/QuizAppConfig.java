package nl.emilvdijk.quizwebgame.config;

import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import nl.emilvdijk.quizwebgame.service.QuizService;
import nl.emilvdijk.quizwebgame.service.QuizServiceAuthenticated;
import nl.emilvdijk.quizwebgame.service.QuizServiceGuest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Configuration
public class QuizAppConfig {

  @Bean
  public AuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }

  @Bean
  @Order(1)
  public QuizService quizService(@AuthenticationPrincipal MyUser myUser){
    if (myUser == null){
      return new QuizServiceGuest();
    }else return new QuizServiceAuthenticated();
  }

}
