package xyz.carmine.raven.exception;

import org.jetbrains.annotations.NotNull;

public class ServiceConnectionException extends Exception {
    public ServiceConnectionException(
            @NotNull String message,
            @NotNull Throwable cause
    ) {
        super(message, cause);
    }
}