package stanuwu.fragx.client.render.font;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import stanuwu.fragx.util.ColorHelper;

import java.awt.*;

/**
 * Draws fonts on the screen and provides information about them.
 */
public class TTFFontRenderer {
    private final TTFFont font;

    private final int SPACING = 2;
    private final Color SHADOW_COLOR = new Color(0, 0, 0, 125);

    /**
     * Creates a new TTFFontRenderer with the given parameters.
     *
     * @param name       Name of the font
     * @param size       Size of the font
     * @param anti_alias Should anti-aliasing be enabled
     */
    public TTFFontRenderer(String name, int size, boolean anti_alias) {
        this.font = new TTFFont(name, size * 2, anti_alias);
    }

    public TTFFontRenderer(String name, int size) {
        this(name, size, true);
    }

    /**
     * Render a text with a shadow on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color       Color of the text
     */
    public void drawStringShadow(MatrixStack matrixStack, String text, float x, float y, float z, Color color) {
        drawString(matrixStack, text, x - (float) this.getWidth(text) + 0.5f, y - (float) this.getHeight() + 0.5f, z, SHADOW_COLOR, SHADOW_COLOR);
        drawString(matrixStack, text, x - (float) this.getWidth(text), y - (float) this.getHeight(), z, color);
    }

    /**
     * Render a centered text on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color       Color of the text
     */
    public void drawCenteredString(MatrixStack matrixStack, String text, float x, float y, float z, Color color) {
        drawString(matrixStack, text, x - (float) this.getWidth(text) / 2, y - (float) this.getHeight() / 2, z, color);
    }

    /**
     * Render a centered text with a shadow on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color       Color of the text
     */
    public void drawCenteredStringShadow(MatrixStack matrixStack, String text, float x, float y, float z, Color color) {
        drawStringShadow(matrixStack, text, x - (float) this.getWidth(text) / 2, y - (float) this.getHeight() / 2, z, color);
    }

    /**
     * Render a text on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color       Color of the text
     */
    public void drawString(MatrixStack matrixStack, String text, float x, float y, float z, Color color) {
        drawString(matrixStack, text, x, y, z, color, color);
    }

    /**
     * Render a text with a shadow on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color1      Color of the start of the gradient
     * @param color1      Color of the end of the gradient
     */
    public void drawStringShadow(MatrixStack matrixStack, String text, float x, float y, float z, Color color1, Color color2) {
        drawString(matrixStack, text, x - (float) this.getWidth(text) + 0.5f, y - (float) this.getHeight() + 0.5f, z, SHADOW_COLOR, SHADOW_COLOR);
        drawString(matrixStack, text, x - (float) this.getWidth(text), y - (float) this.getHeight(), z, color1, color2);
    }

    /**
     * Render a centered text on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color1      Color of the start of the gradient
     * @param color1      Color of the end of the gradient
     */
    public void drawCenteredString(MatrixStack matrixStack, String text, float x, float y, float z, Color color1, Color color2) {
        drawString(matrixStack, text, x - (float) this.getWidth(text) / 2, y - (float) this.getHeight() / 2, z, color1, color2);
    }

    /**
     * Render a centered text with a shadow on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color1      Color of the start of the gradient
     * @param color1      Color of the end of the gradient
     */
    public void drawCenteredStringShadow(MatrixStack matrixStack, String text, float x, float y, float z, Color color1, Color color2) {
        drawStringShadow(matrixStack, text, x - (float) this.getWidth(text) / 2, y - (float) this.getHeight() / 2, z, color1, color2);
    }

    /**
     * Render a text on the screen using the font of the font renderer.
     *
     * @param matrixStack Current matrix stack
     * @param text        Text to render
     * @param x           X position
     * @param y           Y position
     * @param z           Z position
     * @param color1      Starting color of the gradient
     * @param color2      Ending color of the gradient
     */
    public void drawString(MatrixStack matrixStack, String text, float x, float y, float z, Color color1, Color color2) {
        matrixStack.push();
        matrixStack.scale(0.25f, 0.25f, 1);
        x *= 4;
        y *= 4;
        boolean gradient = !color1.equals(color2);

        //set width for gradient calculation (can be skipped if no gradient is present)
        int width = 0;
        if (gradient) width = getRealWidth(text);

        //set up all the rendering parameters (mostly copied from minecraft font renderer)
        int light = LightmapTextureManager.MAX_LIGHT_COORDINATE;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getRenderTypeTextSeeThroughShader);
        RenderSystem.setShaderTexture(0, font.getTexture().getId());
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

        //iterate through characters of the string and draw them next to each other
        float x_offset = x;
        for (char character : text.toCharArray()) {
            //get texture data for given character or if not supported get the placeholder
            TTFFontTexture.CharTextureData charData = font.getTexture().getCharData(character);
            //check if a gradient is present
            if (gradient) {
                //calculate the color of the current position in the gradient
                Color gradient1 = ColorHelper.gradientPoint(color1, color2, x_offset / width);
                Color gradient2 = ColorHelper.gradientPoint(color1, color2, (x_offset + charData.width()) / width);
                x_offset = drawChar(bufferBuilder, matrix, charData, x_offset, y, z, gradient1, gradient2, light) + SPACING;
            } else {
                x_offset = drawChar(bufferBuilder, matrix, charData, x_offset, y, z, color1, color2, light) + SPACING;
            }
        }

        //finish rendering
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        matrixStack.pop();
    }

    private float drawChar(BufferBuilder bufferBuilder, Matrix4f matrix, TTFFontTexture.CharTextureData charData, float x1, float y1, float z, Color color1, Color color2, int light) {
        int c1 = color1.getRGB();
        int c2 = color2.getRGB();

        float x2 = x1 + charData.width();
        float y2 = y1 + charData.height();

        //draw the quad for the character
        bufferBuilder.vertex(matrix, x1, y2, z)
                .color(c1)
                .texture(charData.u(), charData.v2())
                .light(light)
                .next();
        bufferBuilder.vertex(matrix, x2, y2, z)
                .color(c2)
                .texture(charData.u2(), charData.v2())
                .light(light)
                .next();
        bufferBuilder.vertex(matrix, x2, y1, z)
                .color(c2)
                .texture(charData.u2(), charData.v())
                .light(light)
                .next();
        bufferBuilder.vertex(matrix, x1, y1, z)
                .color(c1)
                .texture(charData.u(), charData.v())
                .light(light)
                .next();

        return x2;
    }

    /**
     * Get the width of a string in minecraft screen size when drawn using this font.
     *
     * @param string String to draw
     * @return Width of the string
     */
    public int getWidth(String string) {
        return getRealWidth(string) / 4;
    }

    /**
     * Get the real width of a string when drawn using this font.
     *
     * @param string String to draw
     * @return Width of the string
     */
    private int getRealWidth(String string) {
        int width = 0;
        for (char character : string.toCharArray()) {
            width += font.getTexture().getCharData(character).width();
        }
        return width + SPACING * (string.length() - 1);
    }

    /**
     * Get the height of this font when drawing with it.
     *
     * @return
     */
    public int getHeight() {
        return font.getFontMetrics().getHeight() * font.getFont().getSize() / 8;
    }
}