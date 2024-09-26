package nl.emilvdijk.quizwebgame.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.emilvdijk.quizwebgame.annotation.UserAlreadyExistsConstraint;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MyUserDto {

  @NotEmpty(message = "please enter username")
  @UserAlreadyExistsConstraint(message = "username already exists")
  String username;

  @NotEmpty(message = "please enter password")
  String password;
}
