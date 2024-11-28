package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * base user class implementing spring security UserDetails functionality.
 *
 * @see org.springframework.security.core.userdetails.UserDetails
 * @author Emil van Dijk
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@Data
@NoArgsConstructor
public class MyUser extends BaseEntity implements UserDetails, Serializable {

  @Serial private static final long serialVersionUID = -8443145631573894870L;

  @NonNull private String username;
  @NonNull private String password;
  private boolean enabled;

  @NonNull
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> myRoles;

  @Transient private List<Question> questions = new ArrayList<>();

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<AnsweredQuestion> answeredQuestions;

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
