package main.java.com.xakers.app;

import main.java.com.xakers.datastructures.bintree.BinTree;
import main.java.com.xakers.datastructures.binarysearchtree.BinarySearchTree;
import main.java.com.xakers.model.Seminar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * SeminarDB class represents a database of seminars.
 * Operations include insert, delete, search, and print.
 * based on different attributes such as ID, cost, date, keyword, and location.
 *
 * @author Xavier Akers
 * @version 2025-01-08
 * @since 2025-01-03
 */
public class SeminarDB {
    private final int worldSize;                                // Size of bounding box for the spatial binary tree
    private final BinarySearchTree<Integer, Seminar> idBST;     // BST for searching by seminar ID
    private final BinarySearchTree<Integer, Seminar> costBST;   // BST for searching by seminar cost
    private final BinarySearchTree<String, Seminar> dateBST;    // BST for searching by seminar date
    private final BinarySearchTree<String, Seminar> keywordBST; // BST for searching by seminar keyword
    private final BinTree locationBT;                           // Binary Tree for storing seminar location

    /**
     * Constructor to initialize the SeminarDB with a specified world size.
     *
     * @param worldSize The size of the bounding box for the spatial binary tree.
     */
    public SeminarDB(int worldSize) {
        this.worldSize = worldSize;
        this.idBST = new BinarySearchTree<>();
        this.costBST = new BinarySearchTree<>();
        this.dateBST = new BinarySearchTree<>();
        this.keywordBST = new BinarySearchTree<>();
        this.locationBT = new BinTree(worldSize, worldSize);
    }

    /**
     * Load commands from a file and processes each command (insert, search, delete, print).
     *
     * @param filename The file path containing the commands.
     */
    public void load(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            // Read and process each line
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().trim().split("\\s+");
                if (line.length < 2) {
                    continue; // Skip line with insufficient arguments
                }

                String command = line[0];
                // Process commands based on the first word (command)
                switch (command) {
                    case "insert": {
                        Seminar seminar = parseInputCommand(sc, line); // Parse semiar details and insert
                        processInsert(seminar);
                        break;
                    }
                    case "search": {
                        String type = line[1]; // Type of serach (ID, date, cost, etc.)
                        String[] searchArgs = Arrays.copyOfRange(line, 2, line.length);
                        processSearch(type, searchArgs); // Performs the search
                        break;
                    }
                    case "delete": {
                        int key = Integer.parseInt(line[1]); // ID of seminar to delete
                        processDelete(key); // Delete the seminar
                        break;
                    }
                    case "print": {
                        String type = line[1];
                        processPrint(type); // Print the details of the tree based on type
                        break;
                    }
                    default: {
                        System.err.printf("error: invalid command {%s}\n", command);
                        break;
                    }
                }


            }
        } catch (FileNotFoundException e) {
            // Handle the case where the file cannot be found
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a seminar input command and creates a Seminar object.
     *
     * @param sc   Scanner to read from the command file.
     * @param line The line of input command.
     * @return A new Seminar object created from the parsed data.
     */
    Seminar parseInputCommand(Scanner sc, String[] line) {
        // Parse the seminar details from the input lines
        int id = Integer.parseInt(line[1]);
        String title = sc.nextLine();
        String[] logistics = sc.nextLine().trim().split("\\s+");
        String date = logistics[0];
        String[] keywords = sc.nextLine().trim().split("\\s+");
        String desc = sc.nextLine().trim();

        // Extract Logistics
        int length = Integer.parseInt(logistics[1]);
        short x = Short.parseShort(logistics[2]);
        short y = Short.parseShort(logistics[3]);
        int cost = Integer.parseInt(logistics[4]);

        // Return a new Seminar object
        return new Seminar(id, title, date, length, x, y, cost, keywords, desc);
    }

    /**
     * Processes the insert command to add a seminar to the database.
     *
     * @param seminar The Seminar object to be inserted.
     */
    void processInsert(Seminar seminar) {
        // Check if seminar is within the bounding box
        if ((seminar.x() < 0 || seminar.x() >= this.worldSize) || (seminar.y() < 0 || seminar.y() >= this.worldSize)) {
            System.out.printf("Insert FAILED - Bad x, y coordinates: %d, %d\n", seminar.x(), seminar.y());
            return;
        }

        // Check if the seminar already exists in the database
        if (!idBST.insertUnique(seminar.id(), seminar)) {
            System.out.printf("Insert FAILED - There is already a record with ID %d\n", seminar.id());
            return;
        }

        // Insert seminar into all relevant trees
        costBST.insert(seminar.cost(), seminar);
        dateBST.insert(seminar.date(), seminar);
        for (String keyword : seminar.keywords()) {
            keywordBST.insert(keyword, seminar);
        }
        // Insert seminar into spatial binary tree
        locationBT.insertSeminar(seminar);

        System.out.printf("Successfully inserted record with ID %d\n", seminar.id());
        System.out.println(seminar);
    }

    /**
     * Processes the search command based on the search type and arguments.
     *
     * @param type       The search type (e.g., ID, cost, date, keyword, location).
     * @param searchArgs The arguments for the search.
     */
    void processSearch(String type, String[] searchArgs) {
        if (searchArgs == null || searchArgs.length == 0) {
            System.err.println("error: invalid search arguments");
            return;
        }

        // Perform the search based on the search type
        switch (type) {
            case "ID": {
                if (searchArgs.length != 1) return;

                int key = Integer.parseInt(searchArgs[0]);
                Seminar seminar = idBST.search(key);

                if (seminar == null) {
                    System.out.printf("Search FAILED -- There is no record with ID %d\n", key);
                } else {
                    System.out.printf("Found record with ID %d\n", key);
                    System.out.println(seminar);
                }

                break;
            }
            case "date": {
                if (searchArgs.length != 2) return;

                String low = searchArgs[0];
                String high = searchArgs[1];
                int[] count = {0};
                List<Seminar> seminars = dateBST.search(low, high, count);

                System.out.printf("Seminars with %s in range %s to %s:\n", type, low, high);
                seminars.forEach(System.out::println);
                System.out.printf("%d nodes visited in this search\n", count[0]);
                break;
            }
            case "cost": {
                if (searchArgs.length != 2) return;

                int low = Integer.parseInt(searchArgs[0]);
                int high = Integer.parseInt(searchArgs[1]);
                int[] count = {0};
                List<Seminar> seminars = costBST.search(low, high, count);

                System.out.printf("Seminars with %s in range %d to %d:\n", type, low, high);
                seminars.forEach(System.out::println);
                System.out.printf("%d nodes visited in this search\n", count[0]);
                break;
            }
            case "keyword": {
                if (searchArgs.length != 1) return;

                String keyword = searchArgs[0];
                List<Seminar> seminars = keywordBST.multiSearch(keyword);

                System.out.printf("Seminars matching keyword %s:\n", keyword);
                seminars.forEach(System.out::println);

                break;
            }
            case "location": {
                if (searchArgs.length != 3) return;

                int x = Integer.parseInt(searchArgs[0]);
                int y = Integer.parseInt(searchArgs[1]);
                int radius = Integer.parseInt(searchArgs[2]);

                int[] count = {0};
                List<Seminar> seminars = locationBT.search(x, y, radius, count);

                System.out.printf("Seminars within %d units of %d, %d:\n", radius, x, y);
                for (Seminar seminar : seminars) {
                    System.out.printf("Found a record with key value %d at %d, %d\n",
                            seminar.id(), seminar.x(), seminar.y());
                }
                System.out.printf("%d nodes visited in this search\n", count[0]);
                break;
            }
            default: {
                System.err.println("error: invalid search type");
                break;
            }
        }
    }

    /**
     * Processes the delete command to remove a seminar from the database.
     *
     * @param id The ID of the seminar to be deleted.
     */
    void processDelete(int id) {
        // Remove seminar from the ID BST
        Seminar seminar = idBST.remove(id);
        if (seminar == null) {
            System.out.printf("Delete FAILED -- There is no record with ID %d\n", id);
            return;
        }

        // Remove seminar from other trees and spatial binary tree
        costBST.remove(seminar.cost(), seminar);
        dateBST.remove(seminar.date(), seminar);
        for (String keyword : seminar.keywords()) {
            keywordBST.remove(keyword, seminar);
        }
        locationBT.remove(seminar.id(), seminar.x(), seminar.y());
        System.out.printf("Record with ID %d successfully deleted from the database\n", id);

    }

    /**
     * Processes the print command to display the contents of the trees.
     *
     * @param type The type of tree to print (e.g., ID, cost, date, keyword, location).
     */
    void processPrint(String type) {
        switch (type) {
            case "ID": {
                System.out.println("ID Tree:");
                idBST.dump();
                break;
            }
            case "cost": {
                System.out.println("Cost Tree:");
                costBST.dump();
                break;
            }
            case "date": {
                System.out.println("Date Tree:");
                dateBST.dump();
                break;
            }
            case "keyword": {
                System.out.println("Keyword Tree:");
                keywordBST.dump();
                break;
            }
            case "location": {
                System.out.println("Location Tree:");
                locationBT.dump();
                break;
            }
            default: {
                System.err.println("error: invalid print type");
            }
        }
    }
}
