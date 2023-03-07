package main.java.server.validators;

import main.java.server.exceptions.ValidationException;

public abstract class BaseValidator<T> implements Validator<T> {
    @Override
    public abstract void validate(T t) ;

    /**
     * Check if the given text if not null and not empty
     * @param text the given text string
     * @return true/false if the text is valid or not
     */
    protected boolean validateText(String text) {
        return text != null && !text.trim().isEmpty() && text.matches("[a-zA-Z]+");
    }

    /**
     * Validates string we need to be an email
     * @param email the given email to validate
     * @return true/false if the email is valid or not
     */
    protected boolean validateEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
