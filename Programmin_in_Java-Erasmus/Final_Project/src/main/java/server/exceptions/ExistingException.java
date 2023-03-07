package main.java.server.exceptions;

public class ExistingException extends RuntimeException {
    /**
     * Exception thrown when some data already exists in the repo (or doesn't exist at all)
     * @param message the error message
     */
    public ExistingException(String message) {
        super(message);
    }
}
