package deviceet.common.utils;

import java.util.Random;

public class RandomEnumUtils {
    private static final Random RANDOM = new Random();

    public static <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        return constants[RANDOM.nextInt(constants.length)];
    }
}

