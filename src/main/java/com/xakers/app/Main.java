package main.java.com.xakers.app;

/**
 * Main class to run SeminarTree application.
 * Reads input from command-line arguments to set up a world size and a command file then loads the command file into the SeminarDB controller.
 *
 * @author Xavier Akers
 * @version 2025-01-08
 * @since 2025-01-05
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("command usage : {world-size} {command-file}");
            System.exit(1);
        }
        // Parse world size
        int worldSize = Integer.parseInt(args[0]);

        // Command file path
        String commandFile = args[1];

        // Create new SeminarDB controller with world size
        SeminarDB controller = new SeminarDB(worldSize);

        // Load the commands from the file into the controller
        controller.load(commandFile);
    }
}