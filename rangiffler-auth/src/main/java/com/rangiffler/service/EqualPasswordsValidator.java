package com.rangiffler.service;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.rangiffler.model.EqualPasswords;
import com.rangiffler.model.RegistrationModel;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, RegistrationModel> {
  @Override
  public boolean isValid(RegistrationModel form, ConstraintValidatorContext context) {
    boolean isValid = form.password().equals(form.passwordSubmit());
    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
          .addPropertyNode("password")
          .addConstraintViolation();
    }
    return isValid;
  }
}
