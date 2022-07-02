package stanuwu.fragx.client.render.font;

import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a TTF Font and contains information about that font.
 */
public class TTFFont {
    @Getter
    private final Font font;

    @Getter
    private final FontMetrics fontMetrics;

    @Getter
    private final TTFFontTexture texture;

    /**
     * Create a TTFFont instance for the given font.
     *
     * @param name Name of the font file
     * @param size What size the font should be created in
     */
    public TTFFont(String name, float size, boolean anti_alias) {
        //load font or use default font
        Font font;
        try {
            font = fontFromFile(name);
        } catch (Exception e) {
            e.printStackTrace();
            //default font
            font = new Font("Arial", Font.PLAIN, 16);
        }
        this.font = font.deriveFont(size);
        this.fontMetrics = new Canvas().getFontMetrics(font);
        texture = new TTFFontTexture(this.font, anti_alias);
    }

    /**
     * Load a TTF font from a file.
     *
     * @param name Name of the font file
     * @return Font Object representing the font in the file
     */
    private Font fontFromFile(String name) throws IOException, FontFormatException, NullPointerException {
        final InputStream fontStream = this.getClass().getResourceAsStream("/assets/fragx/font/" + name + ".ttf");
        if (fontStream != null) {
            return Font.createFont(0, fontStream);
        }
        throw new NullPointerException("Font " + name + " was null.");
    }
}