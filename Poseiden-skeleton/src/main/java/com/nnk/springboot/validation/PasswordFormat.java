package com.nnk.springboot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsValidator.class)
public @interface PasswordFormat {
    String message() default "Password must contain 1 uppercase, 1 number and 1 symbol [!@#&()–[{}]:;',?/*~$^+=<>]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
