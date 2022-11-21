package app.Logic;

import java.util.Random;

public class Dice {
    private static final Random random = new Random();

    public static int rollTheDice() {
        return random.nextInt(6) + 1;
    }
}
