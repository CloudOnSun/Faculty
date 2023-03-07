package main.java.server.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    /**
     * Throws an exception when some data is invalid
     * @param message the error message
     */
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}