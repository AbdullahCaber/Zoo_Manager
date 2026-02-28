import java.text.DecimalFormatSymbols;
import java.util.*;
import java.io.*;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
//Manages zoo operations such as loading data, processing commands and coordinating interactions between animals and people.
public class ZooManager {
    // Stores all animals in the zoo, mapped by their name
    private Map<String, Animal> animals = new HashMap<>();
    // Stores all people (visitors or personnel), mapped by their ID
    private Map<String, Person> people = new HashMap<>();
    // Manages the current food stock in the zoo
    private FoodStock foodStock = new FoodStock();
    // Writer for outputting logs to file
    private PrintWriter writer;

    // Constructor initializes writer with UTF-8 encoding
    public ZooManager(String outputFile) throws IOException {
        writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8));    }

    // Loads animals from a file and logs their creation
    public void loadAnimals(String filename) throws IOException {
        writer.println("***********************************");
        writer.println("***Initializing Animal information***");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
        String line;
        // Parses animal type, name, and age
        // Creates corresponding Animal subclass and adds to the map
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0];
            String name = parts[1];
            int age = Integer.parseInt(parts[2]);
            Animal animal = null;
            switch (type) {
                case "Lion":
                    animal = new Lion(name, age);
                    break;
                case "Elephant":
                    animal = new Elephant(name, age);
                    break;
                case "Penguin":
                    animal = new Penguin(name, age);
                    break;
                case "Chimpanzee":
                    animal = new Chimpanzee(name, age);
                    break;
            }
            if (animal != null) {
                animals.put(name, animal);
                writer.println("Added new " + type + " with name " + name + " aged " + age + ".");
            }
        }
        reader.close();
    }

    // Loads people (visitors or personnel) from a file
    public void loadPersons(String filename) throws IOException {
        writer.println("***********************************");
        writer.println("***Initializing Visitor and Personnel information***");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
        String line;
        // Parses person type, name, and ID
        // Adds the person to the people map
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0];
            String name = parts[1];
            String id = parts[2];
            Person person = null;
            switch (type) {
                case "Visitor":
                    person = new Visitor(name, id);
                    break;
                case "Personnel":
                    person = new Personnel(name, id);
                    break;
            }
            if (person != null) {
                people.put(id, person);
                writer.println("Added new " + type + " with id " + id + " and name " + name + ".");
            }
        }
        reader.close();
    }

    // Loads initial food stock from file
    public void loadFood(String filename) throws IOException {
        writer.println("***********************************");
        writer.println("***Initializing Food Stock***");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.000", symbols);
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        // Parses food type and quantity, adds to stock
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0];
            double amount = Double.parseDouble(parts[1]);
            foodStock.addFood(type, amount);
            writer.println("There are " + decimalFormat.format(amount) + " kg of " + type + " in stock");
        }
        reader.close();
    }

    // Processes each command from the command file
    public void processCommands(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.println("***********************************");
            writer.println("***Processing new Command***");
            // Identifies command and dispatches to appropriate handler
            try {
                String[] parts = line.split(",");
                String command = parts[0];
                switch (command) {
                    case "List Food Stock":
                        foodStock.printStock(writer);
                        break;
                    case "Animal Visitation":
                        handleVisitation(parts);
                        break;
                    case "Feed Animal":
                        handleFeeding(parts);
                        break;
                    default:
                        writer.println("Unknown command.");
                }
            }catch (NumberFormatException e) {
                // Handles invalid number format errors
                writer.println("Error processing command: " + line);
                writer.println("Error:" + e.getMessage());
            } catch (Exception e) {
                // Catches all other runtime errors
                writer.println("Error: " + e.getMessage());
            }
        }
        reader.close();
    }

    // Handles visitation-related commands
    private void handleVisitation(String[] parts) throws PersonNotFoundException, AnimalNotFoundException {
        String id = parts[1];
        String animalName = parts[2];
        Person person = people.get(id);
        if (person == null)
            throw new PersonNotFoundException("There are no visitors or personnel with the id " + id);
        if (person instanceof Visitor) {
            writer.println(person.name + " tried  to register for a visit to " + animalName + ".");
        } else {
            writer.println(person.name + " attempts to clean " + animalName + "'s habitat.");
        }
        Animal animal = animals.get(animalName);
        if (animal == null)
            throw new AnimalNotFoundException("There are no animals with the name " + animalName + ".");
        person.visit(animal, writer);
    }

    // Handles feeding-related commands
    private void handleFeeding(String[] parts) throws PersonNotFoundException, AnimalNotFoundException, UnauthorizedFeedingException, NotEnoughFoodException {
        String id = parts[1];
        String animalName = parts[2];
        int meals = Integer.parseInt(parts[3]);
        Person person = people.get(id);
        if (person == null)
            throw new PersonNotFoundException("There are no visitors or personnel with the id " + id);
        Animal animal = animals.get(animalName);
        if (animal == null)
            throw new AnimalNotFoundException("There are no animals with the name " + animalName);
        if (person instanceof Visitor) {
            writer.println(person.name + " tried to feed " + animal.getName());
        }
        person.feed(animal, meals, foodStock, writer);
    }

    // Closes the writer stream after all processing is complete
    public void closeWriter() {
        if (writer != null) {
            writer.close();
        }
    }
}
