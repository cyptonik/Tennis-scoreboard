package org.tennis.scoreboard.exception;

public class AppException extends RuntimeException {
    private final int status;

    public AppException(String message, int status) {
        super(message);
        this.status = status;
    }

    public AppException(Throwable cause, int status) {
        super(cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
