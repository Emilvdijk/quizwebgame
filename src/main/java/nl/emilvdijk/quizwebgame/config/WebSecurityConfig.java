package nl.emilvdijk.quizwebgame.config;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * global security filter configuration class. here the h2 database console, the REST api, and the
 * web security is configured.
 *
 * @author Emil van Dijk
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

  private static final String H2_CONSOLE = "/h2-console/**";

  @Bean
  @Profile({"dev", "test"})
  @Order(1)
  SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(AntPathRequestMatcher.antMatcher(H2_CONSOLE))
        .authorizeHttpRequests(request -> request.requestMatchers(H2_CONSOLE).permitAll())
        .csrf(csrf -> csrf.ignoringRequestMatchers(H2_CONSOLE))
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
    log.warn("h2-console has been made accessible through security");
    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/api/questions/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated());
    return http.build();
  }

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/", "/home", "/quiz", "/images/*", "/register", "error")
                    .permitAll()
                    .requestMatchers("/userPreferences", "/questionHistory")
                    .hasRole("USER")
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
