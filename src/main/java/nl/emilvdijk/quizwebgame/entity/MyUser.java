package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "myusers")
@SuperBuilder
@Data
@NoArgsConstructor
public class MyUser extends BaseEntity implements UserDetails, Serializable {

  @Serial private static final long serialVersionUID = -8443145631573894870L;

  @NonNull private String username;
  @NonNull private String password;
  @NonNull private boolean enabled;

  @NonNull
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
}
