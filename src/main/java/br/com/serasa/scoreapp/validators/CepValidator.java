package br.com.serasa.scoreapp.validators;

import br.com.serasa.scoreapp.validators.annotated.CEP;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CepValidator implements ConstraintValidator<CEP, String> {

    private static final Pattern CEP_PATTER = Pattern.compile("^\\d{8}$");
    @Override
    public void initialize(CEP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(s)) return true;
        return CEP_PATTER.matcher(s).matches();
    }
}
