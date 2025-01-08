package main.java.com.xakers.app;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("command usage : {world-size} {command-file}");
            System.exit(1);
        }
        int worldSize = Integer.parseInt(args[0]);
        String commandFile = args[1];
        SeminarDB controller = new SeminarDB(worldSize);
        controller.load(commandFile);
    }
}