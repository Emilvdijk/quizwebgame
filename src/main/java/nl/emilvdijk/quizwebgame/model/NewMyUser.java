package nl.emilvdijk.quizwebgame.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import nl.emilvdijk.quizwebgame.validator.UserAlreadyExistsConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class NewMyUser {

  @NotEmpty(message = "please enter username")
  @UserAlreadyExistsConstraint(message = "username already exists")
  @Size(min = 3, max = 20, message = "needs to be between 3 and 20 characters")
  String username;

  @NotEmpty(message = "please enter password")
  String password;
}
