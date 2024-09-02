package nl.emilvdijk.quizwebgame.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  //  FIXME overide configure? or make bean?
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/", "/home", "/quiz", "/images/*")
                    .permitAll()
                    .requestMatchers("/h2-console/**") // FIXME remove after testing
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()).httpBasic(withDefaults())
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    //    FIXME remove after testing
    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
    //    http.csrf(csrf -> csrf.disable());
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/quiz/**", "/h2-console/**"));
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    //    FIXME remove this user someday
    //normally we dont show passwords in plaintext here but for demo it is ok we should be using a hashed password instead
    UserDetails user =
        User.withUsername("1").password(passwordEncoder().encode("1")).roles("ADMIN").build();
    UserDetails user2 = User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build();
    return new InMemoryUserDetailsManager(user,user2);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
