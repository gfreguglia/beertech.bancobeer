package br.com.beertechtalents.lupulo.pocmq.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Array;
import java.util.Map;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, String> {
    public void initialize(NullOrNotEmpty constraint) {
    }

    public boolean isValid(String obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        if(!obj.isEmpty()) {
            return true;
        }

        return false;
    }
}
