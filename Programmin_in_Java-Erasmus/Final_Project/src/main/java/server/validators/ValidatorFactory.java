package main.java.server.validators;

public class ValidatorFactory {
    private static ValidatorFactory instance = null;

    private ValidatorFactory() {
    }

    public Validator createValidator(ValidatorType type) {
        return switch (type) {
            case FRIENDSHIP -> new FriendshipValidator();
            case USER -> new UserValidator();
        };
    }

    public static ValidatorFactory getInstance() {
        if (instance == null) instance = new ValidatorFactory();
        return instance;
    }
}
