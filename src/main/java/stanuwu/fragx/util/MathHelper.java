package stanuwu.fragx.util;

import lombok.experimental.UtilityClass;

/**
 * Contains math functions.
 */
@UtilityClass
public class MathHelper {

    /**
     * Round up a number to the next multiple of 2.
     *
     * @param i Number to round
     * @return Next multiple of 2
     */
    public int ceil_binary_power(int i) {
        i--;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        i++;
        return i;
    }
}
