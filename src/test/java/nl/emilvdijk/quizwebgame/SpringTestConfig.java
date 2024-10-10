package nl.emilvdijk.quizwebgame;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(locations = "classpath:applicationTest.properties")
public class SpringTestConfig {}
