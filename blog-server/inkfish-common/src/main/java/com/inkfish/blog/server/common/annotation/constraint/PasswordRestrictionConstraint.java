package com.inkfish.blog.server.common.annotation.constraint;

import com.inkfish.blog.server.common.annotation.PasswordRestriction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author HALOXIAO
 **/
public class PasswordRestrictionConstraint implements ConstraintValidator<PasswordRestriction,String> {
    private static final String ONLY_LETTER_OR_NUMBER = "^[a-z0-9A-Z]+$";


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(ONLY_LETTER_OR_NUMBER);
    }

    @Override
    public void initialize(PasswordRestriction constraintAnnotation) {
    }
}
