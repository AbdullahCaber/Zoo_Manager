// Thrown when a visitor attempts to feed an animal without proper authorization.
public class UnauthorizedFeedingException extends ZooException {
    public UnauthorizedFeedingException(String message) {
        super(message);
    }
}