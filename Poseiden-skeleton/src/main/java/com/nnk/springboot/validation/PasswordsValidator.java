package com.nnk.springboot.validation;

import com.nnk.springboot.domain.UserForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordsValidator implements ConstraintValidator<PasswordFormat, UserForm> {

    //Pattern for password: digit [0-9] - lowercase [a-z] - uppercase [A-Z] - symbol [!@#&()–[{}]:;',?/*~$^+=<>]
    //Length: {8,20}
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(UserForm user, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = pattern.matcher(user.getPassword());
        return matcher.matches();
    }
}
