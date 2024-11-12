package nl.emilvdijk.quizwebgame.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  @Order(1)
  SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        .authorizeHttpRequests(request -> request.requestMatchers("/h2-console/**").permitAll())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
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
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(
                        "/", "/home", "/quiz", "/images/*", "/register", "/authtestpage", "error")
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
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
