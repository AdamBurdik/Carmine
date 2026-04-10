package xyz.carmine.raven.exception;

public class MissingConfigParameterException extends ConfigParsingException {
    public MissingConfigParameterException(String key) {
        super("Config parameter missing: " + key);
    }
}
