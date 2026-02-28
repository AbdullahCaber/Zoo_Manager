import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
// Abstract base class for all animal types.
// Defines common fields (name, age) and behaviors (feeding, cleaning).
public abstract class Animal {
    // Formatter to ensure decimal outputs use dot (.) instead of comma.
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    protected DecimalFormat decimalFormat = new DecimalFormat("0.000", symbols);
    // Basic attributes shared by all animals
    protected String name;
    protected int age;
    // Constructor for setting name and age
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    // Returns the name of the animal
    public String getName() {
        return name;
    }
    // Method to define meal size based on animal type
    public abstract double getMealAmount();
    // Method to feed the animal; must be implemented by each subclass
    public abstract void feed(int NumberOfMeals, FoodStock foodStock, PrintWriter writer) throws NotEnoughFoodException;
    // Method for habitat cleaning instructions
    public abstract void cleanHabitat(PrintWriter writer);
    // Textual representation of the animal instance
    public String toString() {
        return getClass().getSimpleName() + " named " + name + " aged " + age;
    }
}

// Represents a Lion; feeds on meat and has a specific cleaning method
class Lion extends Animal {
    public Lion(String name, int age) {
        super(name, age);
    }
    public double getMealAmount() {
        return 5.0 + (age - 5) * 0.05;
    }
    public void feed(int NumberOfMeals, FoodStock foodStock,PrintWriter writer) throws NotEnoughFoodException {
        double totalMeat = getMealAmount() * NumberOfMeals;
        foodStock.consumeFood("Meat", totalMeat);
        writer.println(name + " has been given " + decimalFormat.format(totalMeat) + " kgs of meat");
    }
    public void cleanHabitat(PrintWriter writer) {
        writer.println("Cleaning " + name + "'s habitat: Removing bones and refreshing sand.");
    }
}

// Represents an Elephant; feeds on plants and has its own cleaning method
class Elephant extends Animal {
    public Elephant(String name, int age) {
        super(name, age);
    }
    public double getMealAmount() {
        return 10.0 + (age - 20) * 0.015;
    }
    public void feed(int NumberOfMeals, FoodStock foodStock, PrintWriter writer) throws NotEnoughFoodException {
        double totalPlant = getMealAmount() * NumberOfMeals;
        foodStock.consumeFood("Plant", totalPlant);
        writer.println(name + " has been given " + decimalFormat.format(totalPlant) + " kgs assorted fruits and hay");
    }
    public void cleanHabitat(PrintWriter writer) {
        writer.println("Cleaning " + name + "'s habitat: Washing the water area.");
    }
}

// Represents a Penguin; feeds on fish and has its own cleaning routine
class Penguin extends Animal {
    public Penguin(String name, int age) {
        super(name, age);
    }
    public double getMealAmount() {
        return 3.0 + (age - 4) * 0.04;
    }
    public void feed(int NumberOfMeals, FoodStock foodStock, PrintWriter writer) throws NotEnoughFoodException {
        double totalFish = getMealAmount() * NumberOfMeals;
        foodStock.consumeFood("Fish", totalFish);
        writer.println(name + " has been given " + decimalFormat.format(totalFish) + " kgs of various kinds of fish");
    }
    public void cleanHabitat(PrintWriter writer) {
        writer.println("Cleaning " + name + "'s habitat: Replenishing ice and scrubbing walls.");
    }
}

// Represents a Chimpanzee; eats both meat and plants; has unique feeding logic
class Chimpanzee extends Animal {
    public Chimpanzee(String name, int age) {
        super(name, age);
    }
    public double getMealAmount() {
        return 6.0 + (age - 10) * 0.025;
    }
    public void feed(int NumberOfMeals, FoodStock foodStock,PrintWriter writer) throws NotEnoughFoodException {
        double total = getMealAmount() * NumberOfMeals;
        double meat = total / 2;
        double plant = total / 2;
        foodStock.consumeFood("Meat", meat);
        foodStock.consumeFood("Plant", plant);
        writer.println(name + " has been given " + decimalFormat.format(meat) + " kgs of meat and " + decimalFormat.format(plant) + " kgs of leaves");
    }
    public void cleanHabitat(PrintWriter writer) {
        writer.println("Cleaning " + name + "'s habitat: Sweeping the enclosure and replacing branches.");
    }
}

// Manages available food stock in the zoo; tracks amounts and handles consumption
class FoodStock {
    private Map<String, Double> stock = new HashMap<>();
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    protected DecimalFormat decimalFormat = new DecimalFormat("0.000", symbols);

    public void addFood(String type, double amount) {
        stock.put(type, stock.getOrDefault(type, 0.000) + amount);
    }
    public void consumeFood(String type, double amount) throws NotEnoughFoodException {
        double available = stock.getOrDefault(type, 0.000);
        if (available < amount) {
            throw new NotEnoughFoodException("Not enough " + type);
        }
        stock.put(type, available - amount);
    }
    public void printStock(PrintWriter writer) {
        writer.println("Listing available Food Stock:");
        writer.println("Plant: " + decimalFormat.format(stock.getOrDefault("Plant", 0.000)) + " kgs");
        writer.println("Fish: " + decimalFormat.format(stock.getOrDefault("Fish", 0.000)) + " kgs");
        writer.println("Meat: " + decimalFormat.format(stock.getOrDefault("Meat", 0.000)) + " kgs");
    }
}
