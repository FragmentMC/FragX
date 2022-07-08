package stanuwu.fragx.client.render.buffer;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

/**
 * Class to help with rendering of vertex buffers.
 */
@UtilityClass
public class BufferRenderer {
    private int vao;
    private int vbo;

    /**
     * Generate the vertex buffer and vertex array object.
     */
    public void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
        });
    }

    /**
     * Bind the vertex buffer.
     */
    public void bindBuffer() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
    }

    /**
     * Unbind the vertex buffer.
     */
    public void unbindBuffer() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Write data to the vertex buffer.
     *
     * @param buffer Data to write to the buffer.
     */
    public void writeBuffer(FloatBuffer buffer) {
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    /**
     * Draw the vertex buffer to the screen.
     *
     * @param drawMode Mode to draw vertex shapes (ex. GL_TRIANGLES)
     * @param verts    Amount of vertices to draw
     */
    public void draw(int drawMode, int verts) {
        glDrawArrays(drawMode, 0, verts);
    }

    /**
     * Bind the vertex array object.
     */
    public void bind() {
        glBindVertexArray(vao);
    }

    /**
     * Unbind the vertex array object.
     */
    public void unbind() {
        glBindVertexArray(0);
    }
}