package nl.emilvdijk.quizwebgame.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;

@Configuration
@EnableJpaAuditing
public class QuizAppConfig {

  @Bean
  public AuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }

//  @Bean
//  @Lazy
//  public QuizService quizService(@AuthenticationPrincipal MyUser myUser){
//    if (myUser == null){
//      return new QuizServiceGuest();
//    }else return new QuizServiceAuthenticated();
//  }

}
