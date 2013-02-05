/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.validation;

import com.google.common.collect.Constraint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DOBValidator implements ConstraintValidator<ValidDOB, String> {

    private Pattern pattern;
    private Matcher matcher;
    private final String DOB_PATTERN = "^\\d{6}$";

    @Override
    public void initialize(ValidDOB constraintAnnotation) {
        pattern = Pattern.compile(DOB_PATTERN);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            return pattern.matcher(value).matches();
        }
    }
}