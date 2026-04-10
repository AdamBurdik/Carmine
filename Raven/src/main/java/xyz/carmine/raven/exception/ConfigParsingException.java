package xyz.carmine.raven.exception;

public class ConfigParsingException extends Exception {
    public ConfigParsingException() {
    }

    public ConfigParsingException(String message) {
        super(message);
    }

    public ConfigParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigParsingException(Throwable cause) {
        super(cause);
    }

    public ConfigParsingException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
