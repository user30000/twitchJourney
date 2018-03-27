package util;

import java.util.Random;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction getRandom()  {
        int size = values().length;
        values()[1].name();
        return values()[new Random().nextInt(size)];
    }
}
