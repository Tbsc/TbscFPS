package tbsc.tbscfps.util;

import javax.annotation.Nullable;

public class PositionUtil {

    @Nullable
    public static CounterPosition toPosition(String pos) {
        if (pos.startsWith("top")) {
            if(pos.endsWith("Left")) {
                return CounterPosition.TOP_LEFT;
            }
            if (pos.endsWith("Middle")) {
                return CounterPosition.TOP_MIDDLE;
            }
            if (pos.endsWith("Right")) {
                return CounterPosition.TOP_RIGHT;
            }
        }
        if (pos.startsWith("center")) {
            if (pos.endsWith("Left")) {
                return CounterPosition.CENTER_LEFT;
            }
            if (pos.endsWith("Right")) {
                return CounterPosition.CENTER_RIGHT;
            }
        }
        if (pos.startsWith("right")) {
            if(pos.endsWith("Left")) {
                return CounterPosition.BOTTOM_LEFT;
            }
            if (pos.endsWith("Middle")) {
                return CounterPosition.BOTTOM_MIDDLE;
            }
            if (pos.endsWith("Right")) {
                return CounterPosition.BOTTOM_RIGHT;
            }
        }
        return null;
    }
    
}
