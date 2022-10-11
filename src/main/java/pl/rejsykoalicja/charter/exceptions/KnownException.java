package pl.rejsykoalicja.charter.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class KnownException extends RuntimeException {
    private final String message;
    private final String description;
    private final UUID errorId;

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
