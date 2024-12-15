package org.example.smartjob.validator;

import org.example.smartjob.entities.User;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EmailValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@dominio\\.cl$";

    public static boolean isValid(User user) {
        if (user.getEmail() == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(user.getEmail());
        return matcher.matches();
    }
}
