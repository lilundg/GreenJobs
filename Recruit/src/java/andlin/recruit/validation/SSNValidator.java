/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SSNValidator implements ConstraintValidator<ValidSSN, String> {

    private Pattern pattern;
    private final String SSN_PATTERN = "^\\d{6}$";

    @Override
    public void initialize(ValidSSN constraintAnnotation) {
        pattern = Pattern.compile(SSN_PATTERN);
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