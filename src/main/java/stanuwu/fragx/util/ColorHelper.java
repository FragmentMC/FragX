package stanuwu.fragx.util;

import lombok.experimental.UtilityClass;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

/**
 * Contains functionality related to colors.
 */
@UtilityClass
public class ColorHelper {
    /**
     * Find the color of any point in a gradient.
     *
     * @param color1   First color of the gradient
     * @param color2   Second color of the gradient
     * @param position Position in the gradient from 0 to 1
     * @return Color of the gradient at the position
     */
    public Color gradientPoint(Color color1, Color color2, float position) {
        position = MathHelper.clamp(position, 0, 1);
        return new Color(
                interpolate(color1.getRed(), color2.getRed(), position),
                interpolate(color1.getGreen(), color2.getGreen(), position),
                interpolate(color1.getBlue(), color2.getBlue(), position),
                interpolate(color1.getAlpha(), color2.getAlpha(), position)
        );
    }

    /**
     * Interpolate between 2 colors.
     *
     * @param color1   First color
     * @param color2   Second color
     * @param position Position between the colors
     * @return Interpolated color
     */
    private int interpolate(int color1, int color2, float position) {
        return (int) (color1 + position * (color2 - color1));
    }

    /**
     * Gives the given color the given alpha value.
     *
     * @param color Color
     * @param alpha Alpha value
     * @return Color with given alpha
     */
    public Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
