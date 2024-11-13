package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;
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

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<AnsweredQuestion> answeredQuestions;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_preferences_id")
  private UserPreferences userPreferences;

  @UpdateTimestamp private Instant lastUpdatedOn;

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
