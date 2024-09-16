package nl.emilvdijk.quizwebgame.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.emilvdijk.quizwebgame.annotation.UserAlreadyExistsConstraint;
import nl.emilvdijk.quizwebgame.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameValidator implements ConstraintValidator<UserAlreadyExistsConstraint, String> {

  @Autowired MyUserService userService;

  @Override
  public void initialize(UserAlreadyExistsConstraint username) {}

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return !userService.checkIfUserExists(s);
  }
}
