package br.com.beertechtalents.lupulo.pocmq.controller.dto.validator;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;


public class CategoryPolicyValidator implements ConstraintValidator<CategoryPolicy, String> {

  @Override
  public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
    try {
      if (!StringUtils.isEmpty(str)) {
        Operacao.Categoria.valueOf(str);
      }

      return true;
    } catch (IllegalArgumentException|NullPointerException e) {
      return false;
    }
  }

}