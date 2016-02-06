package tbsc.tbscfps.util;

import javax.annotation.Nullable;

public enum CounterPosition {

    TOP_LEFT,     TOP_MIDDLE,    TOP_RIGHT,
    CENTER_LEFT        ,        CENTER_RIGHT,
    BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT;

    @Nullable
    public CounterPosition opposite() {
        if (this == TOP_LEFT) return BOTTOM_RIGHT;
        if (this == TOP_MIDDLE) return BOTTOM_RIGHT;
        if (this == TOP_RIGHT) return BOTTOM_LEFT;
        if (this == CENTER_RIGHT) return CENTER_LEFT;

        if (this == BOTTOM_RIGHT) return TOP_LEFT;
        if (this == BOTTOM_MIDDLE) return TOP_MIDDLE;
        if (this == BOTTOM_LEFT) return TOP_RIGHT;
        if (this == CENTER_LEFT) return CENTER_RIGHT;
        return null;
    }

}
