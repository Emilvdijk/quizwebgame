package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
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
public class MyUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long myid;

  private String username;

  private String password;
  private boolean enabled;


  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> myRoles;

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
