package xyz.carmine.cli;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.ChunkSelector;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.minestom.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorldConverter {
    private static void printUsage() {
        System.out.println("Usage: convert <anvil-path> <output-name> [selection-type] [selection-args...]");
        System.out.println("Examples:");
        System.out.println("  convert ~/worlds/my-world polar-my-world");
        System.out.println("  convert ~/worlds/my-world polar-my-world radius 10");
        System.out.println("  convert ~/worlds/my-world polar-my-world radius 0 0 5");
    }

    public static void execute(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
            return;
        }

        // Anvil conversion requires stuff from MinecraftServer,
        // therefore we need to initialize it
        MinecraftServer.init();

        Path anvil = Path.of(args[0]).toAbsolutePath().normalize();
        Path output = Path.of(args[1] + ".polar").toAbsolutePath().normalize();

        ChunkSelector selector = ChunkSelector.all();
        if (args.length > 2) {
            String type = args[2].toLowerCase();
            try {
                if (type.equals("radius")) {
                    if (args.length == 4) {
                        int radius = Integer.parseInt(args[3]);
                        selector = ChunkSelector.radius(radius);
                        System.out.println("Applying selection: Radius of " + radius + " chunks from (0,0)");
                    } else if (args.length == 6) {
                        int centerX = Integer.parseInt(args[3]);
                        int centerZ = Integer.parseInt(args[4]);
                        int radius = Integer.parseInt(args[5]);
                        selector = ChunkSelector.radius(centerX, centerZ, radius);
                        System.out.println("Applying selection: Radius of " + radius + " chunks from (" + centerX + "," + centerZ + ")");
                    } else {
                        System.err.println("Invalid radius arguments.");
                        printUsage();
                        return;
                    }
                } else if (!type.equals("all")) {
                    System.err.println("Unknown selection type: " + type);
                    printUsage();
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing coordinate/radius numbers.");
                printUsage();
                return;
            }
        }

        System.out.println("Converting " + anvil + " → " + output);

        // Pass the dynamically resolved selector here
        PolarWorld polar = AnvilPolar.anvilToPolar(anvil, selector);
        var polarWorldBytes = PolarWriter.write(polar);

        Files.createDirectories(output.getParent());
        Files.write(output, polarWorldBytes);

        System.out.println("Done. Saved at " + output);
    }
}