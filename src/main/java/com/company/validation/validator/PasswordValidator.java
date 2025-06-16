package com.company.validation.validator;

import com.company.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
            "123456", "password", "12345678", "qwerty", "123456789","abc@1234","john@1234","abcd@1234"
    );

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 6) {
            return false;
        }

        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            return false;
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        Pattern specialCharPattern = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean hasSpecial = specialCharPattern.matcher(password).matches();
        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
}
