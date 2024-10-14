package nl.emilvdijk.quizwebgame.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(locations = "classpath:applicationTest.properties")
public class SpringSecurityTestConfig {}
