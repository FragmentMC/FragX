package stanuwu.fragx.client.render.font;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import stanuwu.fragx.util.MathHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Manage the texture atlas for a font.
 */
public class TTFFontTexture {
    @Getter
    private final int id;

    @Getter
    private final CharTextureData[] chars;

    private final int PADDING = 2;

    /**
     * Generate a texture atlas for a font.
     *
     * @param font The font that the texture is for
     */
    public TTFFontTexture(Font font, boolean anti_alias) {
        //get texture id from opengl to write to
        this.id = GlStateManager._genTexture();

        //calculate size of atlas and find all characters the font can render
        int char_count = font.getNumGlyphs();
        FontMetrics fontMetrics = new Canvas().getFontMetrics(font);
        int height = fontMetrics.getHeight();
        int width = 0;
        int max_width = 0;
        ArrayList<Character> valid_chars = new ArrayList<>();
        int i = 0;
        int l = 0;
        for (; i < char_count; l++) {
            char character = (char) l;
            if (font.canDisplay(character)) {
                int char_width = fontMetrics.charWidth(character) + fontMetrics.getMaxAdvance();
                width += char_width;
                if (char_width > max_width) max_width = char_width;
                i++;
                valid_chars.add(character);
            }
        }
        chars = new CharTextureData[l];
        width += PADDING * char_count;
        width += (Math.sqrt(width * height) / height) * (max_width / 2);
        int size = MathHelper.ceil_binary_power((int) Math.ceil(Math.sqrt(width * (height + PADDING))));

        //prepare graphics rendering
        final BufferedImage atlas = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = (Graphics2D) atlas.getGraphics();
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        if (anti_alias) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        //iterate through valid characters and place them on the atlas and map them with their texture data
        int pos_x = 0;
        int pos_y = height;
        for (char character : valid_chars) {
            int char_width = fontMetrics.charWidth(character);
            if (pos_x + char_width > size) {
                pos_x = 0;
                pos_y += height + PADDING;
            }
            graphics.drawString(character + "", pos_x, pos_y - fontMetrics.getDescent());
            chars[character] = new CharTextureData((float) pos_x / size, (float) (pos_y - height) / size, (float) (pos_x + char_width) / size, (float) pos_y / size, char_width, height);
            pos_x += char_width + PADDING + fontMetrics.getMaxAdvance();
        }
        graphics.dispose();

        //upload texture
        RenderSystem.bindTexture(this.id);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager._texImage2D(GL11.GL_TEXTURE_2D, GL11.GL_ZERO, GL11.GL_RGBA, size, size, GL11.GL_ZERO, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, makeBuffer(atlas));
    }

    /**
     * Creates an IntBuffer from a BufferedImage.
     *
     * @param image Image to create a buffer from
     * @return IntBuffer of the image
     */
    private IntBuffer makeBuffer(BufferedImage image) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                int pixel = pixels[y * image.getWidth() + x];
                if (pixel == 0) pixel = 16777215;
                buffer.put((byte) (pixel >> 16 & 0xFF));
                buffer.put((byte) (pixel >> 8 & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) (pixel >> 24 & 0xFF));
            }
        }
        buffer.flip();
        return buffer.asIntBuffer();
    }

    /**
     * Returns the CharTextureData for any given character if the font supports it.
     *
     * @param character Character to get data on
     * @return CharTextureData for the character or default
     */
    public CharTextureData getCharData(char character) {
        if (chars.length < character) {
            return CharTextureData.DEFAULT;
        }
        CharTextureData charData = chars[character];
        return charData == null ? CharTextureData.DEFAULT : charData;
    }

    /**
     * Contains data on where characters are located on a fonts texture atlas.
     */
    public record CharTextureData(float u, float v, float u2, float v2, int width, int height) {
        private static final CharTextureData DEFAULT = new CharTextureData(0, 0, 0, 0, 0, 0);
    }
}