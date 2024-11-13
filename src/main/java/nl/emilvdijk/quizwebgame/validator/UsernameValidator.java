package nl.emilvdijk.quizwebgame.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import nl.emilvdijk.quizwebgame.repository.UserRepo;

@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<UserAlreadyExistsConstraint, String> {

  private final UserRepo userRepo;

  @Override
  public void initialize(UserAlreadyExistsConstraint username) {}

  @Override
  public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
    return !userRepo.existsMyUserByUsername(username);
  }
}
