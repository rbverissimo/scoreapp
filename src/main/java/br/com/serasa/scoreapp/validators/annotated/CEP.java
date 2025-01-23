package br.com.serasa.scoreapp.validators.annotated;

import br.com.serasa.scoreapp.validators.CepValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CepValidator.class)
@Documented
public @interface CEP {

    String message() default "CEP inválido. Digite um telefone válido com 8 dígitos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
