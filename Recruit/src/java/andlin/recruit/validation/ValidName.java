package andlin.recruit.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EmailValidator.class)
@Documented
@Target({ANNOTATION_TYPE, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidName {

    String message() default "{register.name.format}";

    java.lang.Class<?>[] groups() default {};

    java.lang.Class<? extends Payload>[] payload() default {};
}