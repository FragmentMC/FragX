package stanuwu.fragx.client.render.buffer;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import stanuwu.fragx.client.render.scale.ScaleHelper;
import stanuwu.fragx.client.render.shader.Shader;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

/**
 * Assists in drawing 2D shapes and rendering them onto the screen.
 */
public class BufferBuilder2d {
    private final int drawMode;
    private final VertexModes vertexMode;

    private final Shader shader;
    private int texture_id;
    private float pos_scale = 1;

    private boolean color;
    private float r;
    private float g;
    private float b;
    private float a;

    private boolean position;
    private float x;
    private float y;

    private boolean texture;
    private float u;
    private float v;

    private final ArrayList<Float> vertices = new ArrayList<>();
    private final ArrayList<Float> colors = new ArrayList<>();
    private final ArrayList<Float> uv = new ArrayList<>();

    /**
     * Create a new BufferBuilder
     *
     * @param drawMode   Mode to use to create shapes (ex. GL_TRIANGLES)
     * @param vertexMode Format the vertices have
     * @param shader     Shader to use for drawing
     */
    public BufferBuilder2d(int drawMode, VertexModes vertexMode, Shader shader) {
        this.drawMode = drawMode;
        this.vertexMode = vertexMode;
        this.color = false;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 0;
        this.position = false;
        this.x = 0;
        this.y = 0;
        this.shader = shader;
    }

    /**
     * Ready and draw the object to the screen
     */
    public void draw() {
        //bind vao and vbo
        BufferRenderer.bind();
        BufferRenderer.bindBuffer();
        //write buffer data
        BufferRenderer.writeBuffer(getBuffer());
        //add scaled projection matrix
        this.shader.uniformMatrix4f("u_projection", ScaleHelper.getProjectionMatrix());

        //set attributes depending on the vertex mode
        switch (this.vertexMode) {
            case POSITION_COLOR -> {
                addPositionAttribute(0, 0);
                addColorAttribute(1, vertices.size() * 4L);
            }
            case POSITION_COLOR_TEXTURE -> {
                addPositionAttribute(0, 0);
                addColorAttribute(1, vertices.size() * 4L);
                addUVAttribute(2, (vertices.size() + colors.size()) * 4L);
                bindTexture();
            }
            case POSITION_TEXTURE -> {
                addPositionAttribute(0, 0);
                addUVAttribute(1, vertices.size() * 4L);
                bindTexture();
            }
        }

        //unbind the vbo
        BufferRenderer.unbindBuffer();

        //bind the shader and draw the vbo
        this.shader.bind();
        this.drawBuffer();
        this.shader.unbind();

        //unbind the vao
        BufferRenderer.unbind();
    }

    /**
     * Draw the current buffer (separated to be overridden for instanced rendering in the future).
     */
    private void drawBuffer() {
        BufferRenderer.draw(this.drawMode, this.vertices.size());
    }

    /**
     * Set up the position attribute for the shader.
     *
     * @param index   Index of the attribute
     * @param pointer Pointer of the data in the buffer
     */
    private void addPositionAttribute(int index, long pointer) {
        glVertexAttribPointer(index, 2, GL_FLOAT, false, 0, pointer);
        glEnableVertexAttribArray(index);
    }

    /**
     * Set up the color attribute for the shader.
     *
     * @param index   Index of the attribute
     * @param pointer Pointer of the data in the buffer
     */
    private void addColorAttribute(int index, long pointer) {
        glVertexAttribPointer(index, 4, GL_FLOAT, false, 0, pointer);
        glEnableVertexAttribArray(index);
    }

    /**
     * Set up the uv attribute for the shader.
     *
     * @param index   Index of the attribute
     * @param pointer Pointer of the data in the buffer
     */
    private void addUVAttribute(int index, long pointer) {
        glVertexAttribPointer(index, 2, GL_FLOAT, false, 0, pointer);
        glEnableVertexAttribArray(index);
    }

    /**
     * Generate a buffer from all the objects
     *
     * @return Generated buffer
     */
    public FloatBuffer getBuffer() {
        //create a buffer with total size
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size() + colors.size() + uv.size());
        //dump all data into the buffer
        ArrayList<Float> floats = new ArrayList<>();
        floats.addAll(vertices);
        floats.addAll(colors);
        floats.addAll(uv);
        Float[] floatArray = new Float[floats.size()];
        floats.toArray(floatArray);
        buffer.put(ArrayUtils.toPrimitive(floatArray));
        //flip the buffer to ready it for rendering
        return buffer.flip();
    }

    /**
     * Start creation of a vertex.
     *
     * @param x X position of the vertex
     * @param y Y position of the vertex
     * @return used BufferBuilder
     */
    public BufferBuilder2d vert(float x, float y) {
        this.x = x;
        this.y = y;
        this.position = true;
        return this;
    }

    /**
     * Set the color for a vertex.
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return used BufferBuilder
     */
    public BufferBuilder2d color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.color = true;
        return this;
    }

    /**
     * Set the color for a vertex.
     *
     * @param r Red component
     * @param g Green component
     * @param b Blue component
     * @param a Alpha component
     * @return used BufferBuilder
     */
    public BufferBuilder2d color(int r, int g, int b, int a) {
        return color((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    /**
     * Set the color for a vertex.
     *
     * @param color Color object
     * @return used BufferBuilder
     */
    public BufferBuilder2d color(Color color) {
        return color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Set the texture coordinates.
     *
     * @param u Horizontal texture coordinate
     * @param v Vertical texture coordinate
     * @return used BufferBuilder
     */
    public BufferBuilder2d uv(float u, float v) {
        this.u = u;
        this.v = v;
        this.texture = true;
        return this;
    }

    /**
     * Finish the current vertex.
     */
    public void end() {
        try {
            switch (this.vertexMode) {
                case POSITION_COLOR:
                    if (!(addPosition() && addColor())) throw new InvalidVertexException(vertexMode);
                    break;

                case POSITION_COLOR_TEXTURE:
                    if (!(addPosition() && addColor() && addUV())) throw new InvalidVertexException(vertexMode);
                    break;

                case POSITION_TEXTURE:
                    if (!(addPosition() && addUV())) throw new InvalidVertexException(vertexMode);
                    break;
            }
        } catch (InvalidVertexException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a vertex position.
     *
     * @return If position data to be added is present
     */
    private boolean addPosition() {
        if (!this.position) return false;
        vertices.add(x * pos_scale);
        vertices.add(y * pos_scale);
        this.position = false;
        return true;
    }

    /**
     * Add a vertex color.
     *
     * @return If color data to be added is present
     */
    private boolean addColor() {
        if (!this.color) return false;
        colors.add(r);
        colors.add(g);
        colors.add(b);
        colors.add(a);
        this.color = false;
        return true;
    }

    /**
     * Add a vertex uv coordinate.
     *
     * @return If uv data to be added is present
     */
    private boolean addUV() {
        if (!this.texture) return false;
        uv.add(u);
        uv.add(v);
        this.texture = false;
        return true;
    }

    /**
     * Set the texture for a texture shader to use.
     *
     * @param texture GL texture unit id
     */
    public void setTexture(int texture) {
        this.texture_id = texture;
    }

    /**
     * Bind the selected texture
     */
    private void bindTexture() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.texture_id);
        this.shader.uniformValue1i("u_sampler", 0);
    }

    /**
     * Scale of the current object up or down.
     *
     * @param scale Scale to scale to object to
     */
    public void scalePosition(float scale) {
        for (int i = 0; i < vertices.size(); i++) {
            vertices.set(i, vertices.get(i) * scale);
        }
        pos_scale *= scale;
    }
}