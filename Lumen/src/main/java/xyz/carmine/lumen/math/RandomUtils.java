package xyz.carmine.lumen.math;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static int integer(int min, int max) {
        return random.nextInt(min, max);
    }
}
