// Thrown when a person (visitor or personnel) with the given ID is not found in the system.
public class PersonNotFoundException extends ZooException {
    public PersonNotFoundException(String message) {
        super(message);
    }
}