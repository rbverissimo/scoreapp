package br.com.serasa.scoreapp.validators.annotated;


import br.com.serasa.scoreapp.validators.TelefoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TelefoneValidator.class)
@Documented
public @interface Telefone {

    String message() default "Telefone inválido. Digite um telefone válido com 10 ou 11 dígitos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
