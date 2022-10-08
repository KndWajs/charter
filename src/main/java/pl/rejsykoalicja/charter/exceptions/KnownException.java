package pl.rejsykoalicja.charter.exceptions;

import java.util.UUID;

public class KnownException extends RuntimeException {
    private String message;
    private String description;
    private UUID errorId;

    public KnownException(String message) {
        this(message, null, UUID.randomUUID());
    }

    public KnownException(String message, String description, UUID errorId) {
        this.message = message;
        this.description = description;
        this.errorId = errorId;
        //TODO add stacktrace
        //TODO create table for errors
    }
}
