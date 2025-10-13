package org.example.petmatch.User.Validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un símbolo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}