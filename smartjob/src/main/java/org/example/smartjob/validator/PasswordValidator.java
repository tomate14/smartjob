package org.example.smartjob.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class PasswordValidator {

    @Value("${password.regex}")
    private String passwordRegex;

    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
