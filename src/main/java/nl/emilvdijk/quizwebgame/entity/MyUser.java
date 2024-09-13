package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

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

  // https://stackoverflow.com/questions/63451175/how-to-add-data-to-many-to-many-association-with-extra-column-using-jpa-hiberna
@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyUser myUser = (MyUser) o;
    return enabled == myUser.enabled && Objects.equals(id, myUser.id) && Objects.equals(username, myUser.username) && Objects.equals(password, myUser.password) && Objects.equals(myRoles, myUser.myRoles) && Objects.equals(answeredQuestions, myUser.answeredQuestions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, enabled, myRoles, answeredQuestions);
  }
}
