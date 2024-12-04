package ua.orlov.gymtrainerworkload.exception;

public class BusinessLogicException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "A logic error occurred in the application.";

    public BusinessLogicException() {
        super(DEFAULT_MESSAGE);
    }

    public BusinessLogicException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message != null ? message : DEFAULT_MESSAGE, cause);
    }

    public BusinessLogicException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
