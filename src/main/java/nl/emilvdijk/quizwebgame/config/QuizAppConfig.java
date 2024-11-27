package nl.emilvdijk.quizwebgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * global configuration class. thus far only the JpaAuditing is enabled
 *
 * @author Emil van Dijk
 */
@Configuration
@EnableJpaAuditing
public class QuizAppConfig {}
