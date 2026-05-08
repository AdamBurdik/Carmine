package xyz.carmine.raven.core.exception;

public class ServerStartException extends Exception {
    public ServerStartException(String message) {
        super(message);
    }

    public ServerStartException(String message, Throwable cause) {
        super(message, cause);
    }
}
