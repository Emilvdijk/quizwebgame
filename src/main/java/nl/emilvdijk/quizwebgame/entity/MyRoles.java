package nl.emilvdijk.quizwebgame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@Entity
@Table(name = "myroles")
public class MyRoles implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String role;

  public MyRoles(String roles) {
    role = roles;
  }

  @Override
  public String getAuthority() {
    return this.role;
  }
}
