package xyz.carmine.raven.exception;

public class ServerStartException extends Exception {
    public ServerStartException(String message) {
        super(message);
    }

    public ServerStartException(String message, Throwable cause) {
        super(message, cause);
    }
}
