import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        // Validates that the correct number of input arguments are provided
        if (args.length < 5) {
            System.out.println("Usage: java Main <animals.txt> <persons.txt> <foods.txt> <commands.txt> <output.txt>");
            return;
        }
        // Assigns file paths from command-line arguments
        String animalsFile = args[0];
        String personsFile = args[1];
        String foodsFile = args[2];
        String commandsFile = args[3];
        String outputFile = args[4];
        try {
            // Initializes the ZooManager with the output file
            ZooManager zooManager = new ZooManager(outputFile);
            // Loads animal, person, and food data from files
            zooManager.loadAnimals(animalsFile);
            zooManager.loadPersons(personsFile);
            zooManager.loadFood(foodsFile);
            // Processes the commands provided in the command file
            zooManager.processCommands(commandsFile);
            // Closes the writer to finalize output
            zooManager.closeWriter();
        } catch (IOException e) {
            // Handles I/O errors during execution
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}
