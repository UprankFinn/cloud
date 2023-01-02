package de.uprank.cloud.packets.type;

import de.uprank.cloud.packets.ErrorType;

import java.io.Serializable;

public class ErrorPacket implements Serializable {

    private final ErrorType errorType;
    private final String message;

    public ErrorPacket(ErrorType errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return message;
    }
}
