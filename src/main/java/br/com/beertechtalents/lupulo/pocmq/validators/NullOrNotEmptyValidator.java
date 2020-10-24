package br.com.beertechtalents.lupulo.pocmq.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, String> {
    public boolean isValid(String obj, ConstraintValidatorContext context) {
        return (obj == null || !obj.isEmpty());
    }
}
