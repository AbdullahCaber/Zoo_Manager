import java.io.PrintWriter;
// Abstract base class representing a person in the zoo system.
// Each person has a name and ID, and can either be a visitor or personnel.
public abstract class Person {
    protected String name;
    protected String id;
    // Constructor to initialize name and ID
    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }
    // Method for visiting an animal (behavior depends on subclass)
    public abstract void visit(Animal animal, PrintWriter writer);
    // Method for feeding an animal (only allowed for personnel)
    public abstract void feed(Animal animal, int NumberOfMeals, FoodStock stock , PrintWriter writer) throws UnauthorizedFeedingException, NotEnoughFoodException;
    // Returns a string representation of the person
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}

// Represents a visitor at the zoo; can only visit animals, not feed them
class Visitor extends Person {
    public Visitor(String name, String id) {
        super(name, id);
    }
    public void visit(Animal animal,PrintWriter writer) {
        writer.println(name + " successfully visited " + animal.getName() + ".");
    }
    // Throws an error if visitor tries to feed an animal
    public void feed(Animal animal, int NumberOfMeals, FoodStock stock, PrintWriter writer) throws UnauthorizedFeedingException {
        throw new UnauthorizedFeedingException("Visitors do not have the authority to feed animals.");
    }
}

// Represents zoo personnel; allowed to both clean and feed animals
class Personnel extends Person {
    public Personnel(String name, String id) {
        super(name, id);
    }
    // Logs cleaning action and applies cleaning behavior to the animal
    public void visit(Animal animal, PrintWriter writer) {
        writer.println(name + " started cleaning " + animal.getName() + "'s habitat.");
        animal.cleanHabitat(writer);
    }
    public void feed(Animal animal, int NumberOfMeals, FoodStock stock, PrintWriter writer) throws NotEnoughFoodException {
        writer.println(name + " attempts to feed " + animal.getName() + ".");
        animal.feed(NumberOfMeals, stock, writer);
    }
}
