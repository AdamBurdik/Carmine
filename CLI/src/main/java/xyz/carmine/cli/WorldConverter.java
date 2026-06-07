package xyz.carmine.cli;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.minestom.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorldConverter {
    private static void printUsage() {
        System.out.println("Usage: convert <anvil-path> <output-name>");
        System.out.println("Example: convert ~/worlds/my-world polar-my-world");
    }

    public static void execute(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
            return;
        }

        // Anvil conversion requires stuff from MinecraftServer,
        // therefor we need to initialize it
        MinecraftServer.init();

        Path anvil = Path.of(args[0]).toAbsolutePath().normalize();
        Path output = Path.of(args[1] + ".polar").toAbsolutePath().normalize();

        System.out.println("Converting " + anvil + " → " + output);
        PolarWorld polar = AnvilPolar.anvilToPolar(anvil);
        var polarWorldBytes = PolarWriter.write(polar);

        Files.createDirectories(output.getParent());
        Files.write(output, polarWorldBytes);

        System.out.println("Done. Saved at " + output);
    }
}
