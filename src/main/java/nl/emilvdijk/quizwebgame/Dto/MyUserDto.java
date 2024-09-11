package nl.emilvdijk.quizwebgame.Dto;

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

  // FIXME add validation

  @NotEmpty(message = "please enter username")
  @UserAlreadyExistsConstraint(message = "user already exists")
  String username;

  @NotEmpty(message = "please enter password")
  String password;
}
