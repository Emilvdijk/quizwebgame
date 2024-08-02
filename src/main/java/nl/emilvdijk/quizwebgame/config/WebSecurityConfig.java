package nl.emilvdijk.quizwebgame.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            (requests) -> requests.requestMatchers("/", "/home", "/quiz").permitAll());
        //        .formLogin((form) -> form.loginPage("/login").permitAll())
    http
        .formLogin(withDefaults())
        .logout((logout) -> logout.permitAll());

    //    FIXME remove after testing
//    http.csrf(csrf -> csrf.disable());
    http
            .csrf((csrf) -> csrf
                            .ignoringRequestMatchers("/quiz"));
    http.authorizeHttpRequests(
        (requests) -> requests.requestMatchers("/h2-console/**").authenticated());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    //    FIXME remove this user someday
//    UserDetails user =
//        User.withDefaultPasswordEncoder().username("1").password("1").roles("USER").build();
    var user = User.withUsername("1")
            .password(passwordEncoder().encode("1"))
//            .authorities("admin")
            .build();

    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
