package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "myusers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class MyUser implements UserDetails, Serializable {

  @Serial private static final long serialVersionUID = -8443145631573894870L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private String password;
  private boolean enabled;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> myRoles;


  //https://stackoverflow.com/questions/63451175/how-to-add-data-to-many-to-many-association-with-extra-column-using-jpa-hiberna
  @ManyToMany
  @JoinTable(
      name = "user_questions",
      joinColumns = @JoinColumn(name = "myuser_id"),
      inverseJoinColumns = @JoinColumn(name = "question_myid"))
  private List<Question> answeredQuestions;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> userauth = new HashSet<>();
    for (String authority : myRoles) {
      userauth.add(new SimpleGrantedAuthority(authority));
    }
    return userauth;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }
}
