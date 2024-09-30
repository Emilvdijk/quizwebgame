package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.emilvdijk.quizwebgame.enums.ApiChoiceEnum;
import org.hibernate.annotations.CreationTimestamp;
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

  @CreationTimestamp private Instant added;

  // FIXME save in session?
  @Transient private List<Question> questions = new ArrayList<>();

  // https://stackoverflow.com/questions/63451175/how-to-add-data-to-many-to-many-association-with-extra-column-using-jpa-hiberna
  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Question> answeredQuestions;

  private ApiChoiceEnum apiChoiceEnum = ApiChoiceEnum.TRIVIAAPI;

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
