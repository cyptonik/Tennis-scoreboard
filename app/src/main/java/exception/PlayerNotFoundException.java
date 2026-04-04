package exception;

public class PlayerNotFoundException extends AppException{
    public PlayerNotFoundException(String message) {
        super(message, 404);
    }

    public PlayerNotFoundException(String message, Throwable cause) {
        super(cause, 404);
    }
}
