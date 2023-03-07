package main.java.server.validators;

import main.java.server.domain.User;
import main.java.server.exceptions.ValidationException;

public class UserValidator extends BaseValidator<User> {
    /**
     * Checks if the user has valid email and name
     *
     * @param user the given user
     * @throws ValidationException if the user is invalid
     */
    @Override
    public void validate(User user)  {
        String text = "";
        text += validateEmail(user.getEmail()) ? "" : "Email is invalid";
        text += validateText(user.getName()) ? "" : "Name is empty";
        if (!text.isEmpty()) throw new ValidationException(text);
    }
}
