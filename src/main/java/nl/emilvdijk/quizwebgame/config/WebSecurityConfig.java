package nl.emilvdijk.quizwebgame.config;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
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

  @Bean
  @Order(2)
  SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/api/accessDenied", "/api/unauthorized")
                    .permitAll()
                    .requestMatchers("/api/questions/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(exception -> exception.accessDeniedPage("/api/accessDenied"))
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) -> {
                      response.setContentType("application/json");
                      response.setStatus(401);
                      response
                          .getOutputStream()
                          .println("You are not authorized to access this resource.");
                    }));
    return http.build();
  }

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(
                        "/",
                        "/home",
                        "/quiz",
                        "/images/*",
                        "/register",
                        "/error",
                        "/login",
                        "/error403",
                        "/error401")
                    .permitAll()
                    .requestMatchers("/userPreferences", "/questionHistory")
                    .hasRole("USER")
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(exception -> exception.accessDeniedPage("/error403"))
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(authenticationEntryPoint()))
        .httpBasic(withDefaults())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      response.setStatus(401);
      request.getRequestDispatcher("/error401").forward(request, response);
    };
  }
}
