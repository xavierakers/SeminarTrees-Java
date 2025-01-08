package main.java.com.xakers.app;

import main.java.com.xakers.datastructures.bintree.BinTree;
import main.java.com.xakers.datastructures.binarysearchtree.BinarySearchTree;
import main.java.com.xakers.model.Seminar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SeminarDB {
    private final int worldSize;
    private final BinarySearchTree<Integer, Seminar> idBST;
    private final BinarySearchTree<Integer, Seminar> costBST;
    private final BinarySearchTree<String, Seminar> dateBST;
    private final BinarySearchTree<String, Seminar> keywordBST;
    private final BinTree locationBT;

    /**
     * Constructor
     *
     * @param worldSize Bounding box size for BinTree
     */
    public SeminarDB(int worldSize) {
        this.worldSize = worldSize;
        this.idBST = new BinarySearchTree<>();
        this.costBST = new BinarySearchTree<>();
        this.dateBST = new BinarySearchTree<>();
        this.keywordBST = new BinarySearchTree<>();
        this.locationBT = new BinTree(worldSize, worldSize);
    }

    public void load(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().trim().split("\\s+");
                if (line.length < 2) {
                    continue;
                }

                String command = line[0];
                switch (command) {
                    case "insert": {
                        Seminar seminar = parseInputCommand(sc, line);
                        processInsert(seminar);
                        break;
                    }
                    case "search": {
                        String type = line[1];
                        String[] searchArgs = Arrays.copyOfRange(line, 2, line.length);
                        processSearch(type, searchArgs);
                    }
                    break;
                    case "delete": {
                        int key = Integer.parseInt(line[1]);
                        processDelete(key);
                        break;
                    }
                    case "print": {
                        processPrint(line[1]);
                        break;
                    }
                    default: {
                        System.err.printf("error: invalid command {%s}\n", command);
                        break;
                    }
                }


            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    Seminar parseInputCommand(Scanner sc, String[] line) {
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

        return new Seminar(id, title, date, length, x, y, cost, keywords, desc);
    }

    void processInsert(Seminar seminar) {
        // Check bounding box
        if ((seminar.x() < 0 || seminar.x() >= this.worldSize) || (seminar.y() < 0 || seminar.y() >= this.worldSize)) {
            System.out.printf("Insert FAILED - Bad x, y coordinates: %d, %d\n", seminar.x(), seminar.y());
            return;
        }
        // Make sure Seminar has not already been inserted
        if (!idBST.insertUnique(seminar.id(), seminar)) {
            System.out.printf("Insert FAILED - There is already a record with ID %d\n", seminar.id());
            return;
        }

        costBST.insert(seminar.cost(), seminar);
        dateBST.insert(seminar.date(), seminar);

        for (String keyword : seminar.keywords()) {
            keywordBST.insert(keyword, seminar);
        }

        locationBT.insertSeminar(seminar);
        System.out.printf("Successfully inserted record with ID %d\n", seminar.id());
        System.out.println(seminar);
    }

    void processSearch(String type, String[] searchArgs) {
        if (searchArgs == null || searchArgs.length == 0) {
            System.err.println("error: invalid search arguments");
            return;
        }

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

    void processDelete(int id) {
        Seminar seminar = idBST.remove(id);
        if (seminar == null) {
            System.out.printf("Delete FAILED -- There is no record with ID %d\n", id);
            return;
        }

        costBST.remove(seminar.cost(), seminar);
        dateBST.remove(seminar.date(), seminar);
        for (String keyword : seminar.keywords()) {
            keywordBST.remove(keyword, seminar);
        }
        locationBT.remove(seminar.id(), seminar.x(), seminar.y());
        System.out.printf("Record with ID %d successfully deleted from the database\n", id);

    }

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
        }
    }
}
