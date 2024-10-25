package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "myusers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class MyUser extends BaseEntity implements UserDetails, Serializable {

  @Serial private static final long serialVersionUID = -8443145631573894870L;

  private String username;

  private String password;
  private boolean enabled;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> myRoles;

  @Transient private List<Question> questions = new ArrayList<>();

  // https://stackoverflow.com/questions/63451175/how-to-add-data-to-many-to-many-association-with-extra-column-using-jpa-hiberna
  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Question> answeredQuestions;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_preferences_id")
  private UserPreferences userPreferences;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> userAuth = new HashSet<>();
    for (String authority : myRoles) {
      userAuth.add(new SimpleGrantedAuthority(authority));
    }
    return userAuth;
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
