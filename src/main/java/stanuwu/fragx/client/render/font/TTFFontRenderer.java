package stanuwu.fragx.client.render.font;

import stanuwu.fragx.client.render.buffer.BufferBuilder2d;
import stanuwu.fragx.client.render.buffer.VertexModes;
import stanuwu.fragx.client.render.shader.Shaders;
import stanuwu.fragx.util.ColorHelper;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL33.GL_TRIANGLES;

/**
 * Draws fonts on the screen and provides information about them.
 */
public class TTFFontRenderer {
    private final TTFFont font;

    //font is scaled down to prevent it from being pixelated because of minecraft gui scaling
    private final int upscale_factor = 3;
    private final float downscale_factor = 1f / (2 * upscale_factor);

    /**
     * Creates a new TTFFontRenderer with the given parameters.
     *
     * @param name       Name of the font
     * @param size       Size of the font
     * @param anti_alias Should anti-aliasing be enabled
     */
    public TTFFontRenderer(String name, int size, boolean anti_alias) {
        this.font = new TTFFont(name, size * upscale_factor, anti_alias);
    }

    public TTFFontRenderer(String name, int size) {
        this(name, size, true);
    }

    /**
     * Render a centered text on the screen using the font of the font renderer.
     *
     * @param text  Text to render
     * @param x     X position
     * @param y     Y position
     * @param color Color of the text
     */
    public void drawCenteredString(String text, float x, float y, Color color) {
        drawString(text, x - this.getWidth(text) / 2, y - this.getHeight() / 2, color);
    }

    /**
     * Render a text on the screen using the font of the font renderer.
     *
     * @param text  Text to render
     * @param x     X position
     * @param y     Y position
     * @param color Color of the text
     */
    public void drawString(String text, float x, float y, Color color) {
        drawString(text, x, y, color, color, color, color);
    }

    /**
     * Render a centered text on the screen using the font of the font renderer.
     *
     * @param text   Text to render
     * @param x      X position
     * @param y      Y position
     * @param color1 Top left color
     * @param color2 Bottom left color
     * @param color3 Top right color
     * @param color4 Bottom right color
     */
    public void drawCenteredString(String text, float x, float y, Color color1, Color color2, Color color3, Color color4) {
        drawString(text, x - this.getWidth(text) / 2, y - this.getHeight() / 2, color1, color2, color3, color4);
    }

    /**
     * Render a text on the screen using the font of the font renderer.
     *
     * @param text   Text to render
     * @param x      X position
     * @param y      Y Position
     * @param color1 Top left color
     * @param color2 Bottom left color
     * @param color3 Top right color
     * @param color4 Bottom right color
     */
    public void drawString(String text, float x, float y, Color color1, Color color2, Color color3, Color color4) {
        int width = getRealWidth(text);
        x *= upscale_factor * 2;
        y *= upscale_factor * 2;

        //create buffer and bind texture atlas of the font
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        BufferBuilder2d buffer = new BufferBuilder2d(GL_TRIANGLES, VertexModes.POSITION_COLOR_TEXTURE, Shaders.getPositionColorTextureShader());
        buffer.setTexture(this.font.getTexture().getId());
        buffer.scalePosition(downscale_factor);

        //iterate through characters of the string and draw them next to each other
        float x_offset = x;
        for (char character : text.toCharArray()) {
            //get texture data for given character or if not supported get the placeholder
            TTFFontTexture.CharTextureData charData = font.getTexture().getCharData(character);
            //calculate the color of the current position in the gradient
            Color gradient1 = ColorHelper.gradientPoint(color1, color3, x_offset / width);
            Color gradient2 = ColorHelper.gradientPoint(color2, color4, x_offset / width);
            Color gradient3 = ColorHelper.gradientPoint(color1, color3, (x_offset + charData.width()) / width);
            Color gradient4 = ColorHelper.gradientPoint(color2, color4, (x_offset + charData.width()) / width);
            x_offset = drawChar(buffer, charData, x_offset, y, gradient1, gradient2, gradient3, gradient4);

        }
        buffer.draw();
    }

    private float drawChar(BufferBuilder2d buffer, TTFFontTexture.CharTextureData charData, float x, float y, Color color1, Color color2, Color color3, Color color4) {
        float x2 = x + charData.width();
        float y2 = y + charData.height();

        //draw the quad for the character
        buffer.vert(x, y2).color(color1).uv(charData.u(), charData.v()).end();
        buffer.vert(x, y).color(color2).uv(charData.u(), charData.v2()).end();
        buffer.vert(x2, y).color(color4).uv(charData.u2(), charData.v2()).end();
        buffer.vert(x2, y).color(color4).uv(charData.u2(), charData.v2()).end();
        buffer.vert(x2, y2).color(color3).uv(charData.u2(), charData.v()).end();
        buffer.vert(x, y2).color(color1).uv(charData.u(), charData.v()).end();

        //return the position of the end of the character
        return x2;
    }

    /**
     * Get the width of a string in minecraft screen size when drawn using this font.
     *
     * @param string String to draw
     * @return Width of the string
     */
    public float getWidth(String string) {
        return getRealWidth(string) * downscale_factor;
    }

    /**
     * Get the unscaled width of a string when drawn using this font.
     *
     * @param string String to draw
     * @return Width of the string
     */
    private int getRealWidth(String string) {
        int width = 0;
        for (char character : string.toCharArray()) {
            width += font.getTexture().getCharData(character).width();
        }
        return width;
    }

    /**
     * Get the height of this font when drawing with it.
     *
     * @return Height of the font
     */
    public float getHeight() {
        return font.getTexture().getCharData('0').height() * downscale_factor;
    }
}