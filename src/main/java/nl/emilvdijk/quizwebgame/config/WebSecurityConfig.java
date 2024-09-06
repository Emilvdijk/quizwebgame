package nl.emilvdijk.quizwebgame.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;


import nl.emilvdijk.quizwebgame.entity.MyUser;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import nl.emilvdijk.quizwebgame.service.QuizService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  //  FIXME overide configure? or make bean?
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests // TODO maybe clean this up?
                    .requestMatchers(
                        "/", "/home", "/quiz", "/images/*", "registration", "/h2-console/**")
                    .permitAll()
            //                    .requestMatchers("/h2-console/**") // FIXME remove after testing
            //                    .hasRole("ADMIN")
            //                    .anyRequest()
            //                    .authenticated())
            )
        .httpBasic(withDefaults())
        .formLogin(form -> form.loginPage("/login").permitAll())
        .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    //    FIXME remove after testing
    http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
    //    http.csrf(csrf -> csrf.disable());
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/quiz/**", "/h2-console/**"));

    return http.build();
  }

  //  @Bean
  //  public UserDetailsService userDetailsService() {
  //    //    FIXME remove this user someday
  //    //normally we dont show passwords in plaintext here but for demo it is ok we should be using
  // a hashed password instead
  //    UserDetails user =
  //        User.withUsername("1").password(passwordEncoder().encode("1")).roles("ADMIN").build();
  //    UserDetails user2 =
  // User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build();
  //    return new JdbcUserDetailsManager(user,user2);
  //  }

//  @Bean
//  public MyUserService myUserService(DataSource dataSource) {
//    MyUserService myUserService = new MyUserService();
//
//    //    ArrayList<String> userauth = new ArrayList<>();
//    //    userauth.add("USER");
//    //    UserDetails user =
//    //        MyUser.builder()
//    //            .username("user")
//    //            .password(passwordEncoder().encode("user"))
//    //            .authorities(userauth)
//    //            .build();
//
////    ArrayList<String> adminauth = new ArrayList<>();
////    adminauth.add("ADMIN");
////
////    List<MyRoles> nice = new ArrayList<>();
////    nice.add(new MyRoles("ADMIN"));
////    MyUser admin =
////        MyUser.builder()
////            .username("nice12")
////            .password(passwordEncoder().encode("nice12"))
////            .myRoles(nice)
////            .build();
////    myUserService.save(admin);
//ArrayList<String> roles = new ArrayList<>();
//roles.add("USER");
//    MyUser user = MyUser.builder().username("user").password(passwordEncoder().encode("user")).myRoles(roles).build();
//    myUserService.save(user);
//    //    UserDetailsManager.createUser(user);
//
//    return myUserService;
//  }

  //  @Bean
  //  public UserDetailsManager users(DataSource dataSource) {
  //    UserDetails user =
  //        User.withDefaultPasswordEncoder()
  //            .username("user")
  //            .password("password")
  //            .roles("USER")
  //            .build();
  //    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
  //    users.createUser(user);
  //    return users;
  //  }

  //    @Bean
  //    public DataSource dataSource() {
  //      return new EmbeddedDatabaseBuilder()
  //          .setType(H2)
  //          .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
  //          .build();
  //    }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
