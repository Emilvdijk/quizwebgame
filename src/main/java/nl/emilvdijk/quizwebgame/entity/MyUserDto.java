package nl.emilvdijk.quizwebgame.entity;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  @Min(value = 5, message = "username needs to be at least 5 characters long")
  @UserAlreadyExistsConstraint(message = "user already exists")
  String username;

  @Min(value = 5, message = "passwords needs to be at least 5 characters long")
  @NotEmpty(message = "please enter password")
  String password;
}
