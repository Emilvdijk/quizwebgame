package nl.emilvdijk.quizwebgame.config;

import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(locations = "classpath:applicationTest.properties")
public class SpringSecurityTestConfig {}
