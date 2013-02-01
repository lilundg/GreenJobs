/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.validation;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author pinballmilitia
 */
public class EmailConstraint implements ConstraintValidator<Annotation, Object> {

    private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    private Pattern pattern;
    
    @Override
    public void initialize(Annotation constraintAnnotation) {
        pattern = Pattern.compile(EMAIL_PATTERN);
       
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
         return pattern.matcher(value.toString()).matches(); 
    }
    
    
}
