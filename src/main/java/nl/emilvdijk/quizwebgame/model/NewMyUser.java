package nl.emilvdijk.quizwebgame.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.emilvdijk.quizwebgame.validator.UserAlreadyExistsConstraint;

/**
 * model for a new user form. it includes several validation constraints by Jakarta.Validation and
 * one custom-made one.
 *
 * @see UserAlreadyExistsConstraint
 * @see nl.emilvdijk.quizwebgame.validator.UsernameValidator
 * @author Emil van Dijk
 */
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
