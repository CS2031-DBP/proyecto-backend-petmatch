package org.example.petmatch.User.Validation;

import jakarta.validation.ConstraintValidator;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, jakarta.validation.ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            return false;
        }
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            return false;
        }
        if (!password.chars().anyMatch(Character::isDigit)) {
            return false;
        }
        if (!password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;:',.<>?/`~\"\\-=".indexOf(ch) >= 0)) {
            return false;
        }
        return true;
    }
}
