package xyz.carmine.cli;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    private static void printUsage() {
        System.out.println("Error: Invalid or missing arguments.");
        System.out.println("Available commands:");
        System.out.println("  convert <anvil-path> <output-name> - Converts an Anvil world to Polar format.");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java -jar cli.jar convert ~/worlds/my-world polar-my-world");
    }

    static void main(String[] args) throws IOException {
        if (args.length < 1) {
            printUsage();
            return;
        }

        switch (args[0]) {
            case "convert" -> WorldConverter.execute(Arrays.copyOfRange(args, 1, args.length));
            default -> printUsage();
        }
    }
}
