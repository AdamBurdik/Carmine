package xyz.carmine.raven.core.exception;

public class MissingConfigParameterException extends ConfigParsingException {
    public MissingConfigParameterException(String key) {
        super("Config parameter missing: " + key);
    }
}
