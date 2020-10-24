package br.com.beertechtalents.lupulo.pocmq.controller.dto.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CategoryPolicyValidator.class})
@Documented
public @interface CategoryPolicy {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
