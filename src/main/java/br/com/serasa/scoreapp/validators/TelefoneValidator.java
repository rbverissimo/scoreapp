package br.com.serasa.scoreapp.validators;

import br.com.serasa.scoreapp.validators.annotated.Telefone;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TelefoneValidator implements ConstraintValidator<Telefone, String> {

    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\d{10,11}$");

    @Override
    public void initialize(Telefone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(s)) return true;
        return TELEFONE_PATTERN.matcher(s).matches();
    }
}
