package xyz.carmine.raven.core.exception;

public class MenuParsingException extends Exception {
    public MenuParsingException() {
        super();
    }

    public MenuParsingException(String message) {
        super(message);
    }

    public MenuParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuParsingException(Throwable cause) {
        super(cause);
    }

    protected MenuParsingException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
