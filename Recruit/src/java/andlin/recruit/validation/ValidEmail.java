package andlin.recruit.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

@Constraint(validatedBy = EmailValidator.class)
@Documented
@Target({ANNOTATION_TYPE, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidEmail {

    String message() default "{register.email.format}";

    java.lang.Class<?>[] groups() default {};

    java.lang.Class<? extends Payload>[] payload() default {};
}