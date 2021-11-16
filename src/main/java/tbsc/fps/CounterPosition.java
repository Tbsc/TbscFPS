package tbsc.fps;

import java.util.Arrays;

public enum CounterPosition {

    TOP_LEFT,     TOP_MIDDLE,    TOP_RIGHT,
                                CENTER_RIGHT,
                                BOTTOM_RIGHT,
                 BOTTOM_MIDDLE,
    BOTTOM_LEFT,
    CENTER_LEFT;

    public static String[] names() {
        return Arrays.stream(values()).map(Enum::name).toArray(String[]::new);
    }

}
