package nl.emilvdijk.quizwebgame.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nl.emilvdijk.quizwebgame.validator.UsernameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAlreadyExistsConstraint {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
