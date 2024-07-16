package nl.emilvdijk.quizwebgame.config;

import nl.emilvdijk.quizwebgame.entity.Question;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class QuestionConfig {
  @Bean
  public Question Question() {
    //    FIXME configure question bean
    return new Question();
  }
}
