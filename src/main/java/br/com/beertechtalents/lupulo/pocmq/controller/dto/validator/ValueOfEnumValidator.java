package br.com.beertechtalents.lupulo.pocmq.controller.dto.validator;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
  private Pattern pattern;

  private List<String> acceptedValues;

  @Override
  public void initialize(ValueOfEnum annotation) {
    acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return acceptedValues.contains(value.toString());
  }

}