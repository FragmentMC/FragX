package stanuwu.fragx.client.render;

import lombok.experimental.UtilityClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import stanuwu.fragx.client.render.buffer.BufferBuilder2d;
import stanuwu.fragx.client.render.buffer.VertexModes;
import stanuwu.fragx.client.render.scale.ScaleHelper;
import stanuwu.fragx.client.render.shader.Shader;
import stanuwu.fragx.client.render.shader.Shaders;

import java.awt.*;

import static org.lwjgl.opengl.GL33.*;

/**
 * Contains pre-made objects that can be drawn to the screen.
 */
@UtilityClass
public class Render2d {
    /**
     * Enable blend.
     */
    private void enableBlend() {
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Get the opengl texture unit id for a minecraft texture.
     *
     * @param texture Texture
     * @return Opengl texture unit id
     */
    private int getGlTexture(Identifier texture) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(texture);
        return abstractTexture.getGlId();
    }

    /**
     * Draw a rectangle from vertices.
     *
     * @param buffer Buffer builder to draw the rectangle in
     * @param x      Left position
     * @param y      Bottom position
     * @param x2     Right position
     * @param y2     Top position
     * @param color1 Top left color
     * @param color2 Bottom left color
     * @param color3 Top right color
     * @param color4 Bottom right color
     */
    private void draw_rect(BufferBuilder2d buffer, float x, float y, float x2, float y2, Color color1, Color color2, Color color3, Color color4) {
        buffer.vert(x, y2).color(color1).end();
        buffer.vert(x, y).color(color2).end();
        buffer.vert(x2, y).color(color4).end();
        buffer.vert(x2, y).color(color4).end();
        buffer.vert(x2, y2).color(color3).end();
        buffer.vert(x, y2).color(color1).end();
    }

    /**
     * Draw a rectangle to the screen.
     *
     * @param x      X position
     * @param y      Y position
     * @param width  Width
     * @param height Height
     * @param color  Color
     */
    public void rect(float x, float y, float width, float height, Color color) {
        rect(x, y, width, height, color, color, color, color);
    }

    /**
     * Draw a rectangle to the screen.
     *
     * @param x      X position
     * @param y      Y position
     * @param width  Width
     * @param height Height
     * @param color1 Top left color
     * @param color2 Bottom left color
     * @param color3 Top right color
     * @param color4 Bottom right color
     */
    public void rect(float x, float y, float width, float height, Color color1, Color color2, Color color3, Color color4) {
        enableBlend();
        float x2 = x + width, y2 = y + height;
        BufferBuilder2d buffer = new BufferBuilder2d(GL_TRIANGLES, VertexModes.POSITION_COLOR, Shaders.getPositionColorShader());
        draw_rect(buffer, x, y, x2, y2, color1, color2, color3, color4);
        buffer.draw();
    }

    /**
     * Draw a rounded rectangle to the screen.
     *
     * @param x      X position
     * @param y      Y position
     * @param width  Width
     * @param height Height
     * @param radius Radius
     * @param color  Color
     */
    public void rounded_rect(float x, float y, float width, float height, float radius, Color color) {
        rounded_rect(x, y, width, height, radius, color, color, color, color);
    }

    /**
     * Draw a rounded rectangle to the screen.
     *
     * @param x      X position
     * @param y      Y position
     * @param width  Width
     * @param height Height
     * @param radius Radius
     * @param color1 Top left color
     * @param color2 Bottom left color
     * @param color3 Top right color
     * @param color4 Bottom right color
     */
    public void rounded_rect(float x, float y, float width, float height, float radius, Color color1, Color color2, Color color3, Color color4) {
        enableBlend();
        float x2 = x + width, y2 = y + height;
        float scale = ScaleHelper.getScale();
        float offset = 3 * scale;
        Shader shader = Shaders.getRoundedRectPositionColorShader();
        shader.uniformValue1f("u_radius", radius * scale);
        shader.uniformValue2f("u_size", width * scale - offset, height * scale - offset);
        shader.uniformValue2f("u_position", x * scale + offset / 2, y * scale + offset / 2);
        BufferBuilder2d buffer = new BufferBuilder2d(GL_TRIANGLES, VertexModes.POSITION_COLOR, shader);
        draw_rect(buffer, x, y, x2, y2, color1, color2, color3, color4);
        buffer.draw();
    }

    /**
     * Draw a colored rectangle with a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param texture Texture
     * @param color   Color
     */
    public void color_texture_rect_full(float x, float y, float width, float height, Identifier texture, Color color) {
        color_texture_rect_full(x, y, width, height, getGlTexture(texture), color);
    }

    /**
     * Draw a colored rectangle with a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param texture Texture
     * @param color1  Top left color
     * @param color2  Bottom left color
     * @param color3  Top right color
     * @param color4  Bottom right color
     */
    public void color_texture_rect_full(float x, float y, float width, float height, Identifier texture, Color color1, Color color2, Color color3, Color color4) {
        color_texture_rect_full(x, y, width, height, getGlTexture(texture), color1, color2, color3, color4);
    }

    /**
     * Draw a colored rectangle with part a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param u       Left texture coordinate
     * @param v       Top texture coordinate
     * @param u2      Right texture coordinate
     * @param v2      Bottom texture coordinate
     * @param texture Texture
     * @param color   Color
     */
    public void color_texture_rect(float x, float y, float width, float height, float u, float v, float u2, float v2, Identifier texture, Color color) {
        color_texture_rect(x, y, width, height, u, v, u2, v2, getGlTexture(texture), color);
    }

    /**
     * Draw a colored rectangle with part a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param u       Left texture coordinate
     * @param v       Top texture coordinate
     * @param u2      Right texture coordinate
     * @param v2      Bottom texture coordinate
     * @param texture Texture
     * @param color1  Top left color
     * @param color2  Bottom left color
     * @param color3  Top right color
     * @param color4  Bottom right color
     */
    public void color_texture_rect(float x, float y, float width, float height, float u, float v, float u2, float v2, Identifier texture, Color color1, Color color2, Color color3, Color color4) {
        color_texture_rect(x, y, width, height, u, v, u2, v2, getGlTexture(texture), color1, color2, color3, color4);
    }

    /**
     * Draw a colored rectangle with a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param texture Opengl texture unit id
     * @param color   Color
     */
    public void color_texture_rect_full(float x, float y, float width, float height, int texture, Color color) {
        color_texture_rect(x, y, width, height, 0, 0, 1, 1, texture, color);
    }

    /**
     * Draw a colored rectangle with a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param texture Opengl texture unit id
     * @param color1  Top left color
     * @param color2  Bottom left color
     * @param color3  Top right color
     * @param color4  Bottom right color
     */
    public void color_texture_rect_full(float x, float y, float width, float height, int texture, Color color1, Color color2, Color color3, Color color4) {
        color_texture_rect(x, y, width, height, 0, 0, 1, 1, texture, color1, color2, color3, color4);
    }

    /**
     * Draw a colored rectangle with part a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param u       Left texture coordinate
     * @param v       Top texture coordinate
     * @param u2      Right texture coordinate
     * @param v2      Bottom texture coordinate
     * @param texture Opengl texture unit id
     * @param color   Color
     */
    public void color_texture_rect(float x, float y, float width, float height, float u, float v, float u2, float v2, int texture, Color color) {
        color_texture_rect(x, y, width, height, u, v, u2, v2, texture, color, color, color, color);
    }

    /**
     * Draw a colored rectangle with part a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param u       Left texture coordinate
     * @param v       Top texture coordinate
     * @param u2      Right texture coordinate
     * @param v2      Bottom texture coordinate
     * @param texture Opengl texture unit id
     * @param color1  Top left color
     * @param color2  Bottom left color
     * @param color3  Top right color
     * @param color4  Bottom right color
     */
    public void color_texture_rect(float x, float y, float width, float height, float u, float v, float u2, float v2, int texture, Color color1, Color color2, Color color3, Color color4) {
        enableBlend();
        float x2 = x + width, y2 = y + height;
        BufferBuilder2d buffer = new BufferBuilder2d(GL_TRIANGLES, VertexModes.POSITION_COLOR_TEXTURE, Shaders.getPositionColorTextureShader());
        buffer.setTexture(texture);
        buffer.vert(x, y2).color(color1).uv(u, v).end();
        buffer.vert(x, y).color(color2).uv(u, v2).end();
        buffer.vert(x2, y).color(color4).uv(u2, v2).end();
        buffer.vert(x2, y).color(color4).uv(u2, v2).end();
        buffer.vert(x2, y2).color(color3).uv(u2, v).end();
        buffer.vert(x, y2).color(color1).uv(u, v).end();
        buffer.draw();
    }

    /**
     * Draw a rectangle with a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param texture Texture
     */
    public void texture_rect_full(float x, float y, float width, float height, Identifier texture) {
        texture_rect_full(x, y, width, height, getGlTexture(texture));
    }

    /**
     * Draw a rectangle with part a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param u       Left texture coordinate
     * @param v       Top texture coordinate
     * @param u2      Right texture coordinate
     * @param v2      Bottom texture coordinate
     * @param texture Texture
     */
    public void texture_rect(float x, float y, float width, float height, float u, float v, float u2, float v2, Identifier texture) {
        texture_rect(x, y, width, height, u, v, u2, v2, getGlTexture(texture));
    }

    /**
     * Draw a rectangle with a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param texture Opengl texture unit id
     */
    public void texture_rect_full(float x, float y, float width, float height, int texture) {
        texture_rect(x, y, width, height, 0, 0, 1, 1, texture);
    }

    /**
     * Draw a rectangle with part a texture.
     *
     * @param x       X position
     * @param y       Y position
     * @param width   Width
     * @param height  Height
     * @param u       Left texture coordinate
     * @param v       Top texture coordinate
     * @param u2      Right texture coordinate
     * @param v2      Bottom texture coordinate
     * @param texture Opengl texture unit id
     */
    public void texture_rect(float x, float y, float width, float height, float u, float v, float u2, float v2, int texture) {
        enableBlend();
        float x2 = x + width, y2 = y + height;
        BufferBuilder2d buffer = new BufferBuilder2d(GL_TRIANGLES, VertexModes.POSITION_TEXTURE, Shaders.getPositionTextureShader());
        buffer.setTexture(texture);
        buffer.vert(x, y2).uv(u, v).end();
        buffer.vert(x, y).uv(u, v2).end();
        buffer.vert(x2, y).uv(u2, v2).end();
        buffer.vert(x2, y).uv(u2, v2).end();
        buffer.vert(x2, y2).uv(u2, v).end();
        buffer.vert(x, y2).uv(u, v).end();
        buffer.draw();
    }
}
