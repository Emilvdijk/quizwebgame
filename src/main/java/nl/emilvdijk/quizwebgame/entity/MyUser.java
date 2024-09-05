package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
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

  //  @ElementCollection
  //  @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "OWNER_ID"))
  //  @Column(name = "authoritiesnumber")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<MyRoles> authorities;

  //  @Override
  //  public Collection<? extends GrantedAuthority> getAuthorities() {
  //    Set<GrantedAuthority> userauth = new HashSet<>();
  //    for (String authority : authorities) {
  //      userauth.add(new SimpleGrantedAuthority(authority));
  //    }
  //    return userauth;
  //  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
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
