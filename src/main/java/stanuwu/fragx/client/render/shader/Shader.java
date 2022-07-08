package stanuwu.fragx.client.render.shader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

/**
 * Represents an opengl shader program (vertex and fragment shader)
 */
public class Shader {
    private final int id;

    /**
     * Create a new shader program for a shader.
     *
     * @param name Filename of the shader
     */
    public Shader(String name) {
        int vertexShader = Shaders.loadShaderProgram(name, Shaders.ShaderType.VERTEX);
        int fragmentShader = Shaders.loadShaderProgram(name, Shaders.ShaderType.FRAGMENT);
        id = glCreateProgram();
        glAttachShader(id, vertexShader);
        glAttachShader(id, fragmentShader);
        glLinkProgram(id);
    }

    /**
     * Bind the shader.
     */
    public void bind() {
        glUseProgram(id);
    }

    /**
     * Unbind the shader.
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Pass a uniform float value into the shader.
     *
     * @param name  Name of the uniform
     * @param value Value of the uniform
     */
    public void uniformValue1f(String name, float value) {
        bind();
        glUniform1f(glGetUniformLocation(id, name), value);
        unbind();
    }

    /**
     * Pass a uniform vec2 value into the shader.
     *
     * @param name   Name of the uniform
     * @param value1 First value of the uniform
     * @param value2 Second value of the uniform
     */
    public void uniformValue2f(String name, float value1, float value2) {
        bind();
        glUniform2f(glGetUniformLocation(id, name), value1, value2);
        unbind();
    }

    /**
     * Pass a uniform matrix4f value into the shader.
     *
     * @param name   Name of the uniform
     * @param matrix Value of the uniform
     */
    public void uniformMatrix4f(String name, FloatBuffer matrix) {
        bind();
        glUniformMatrix4fv(glGetUniformLocation(id, name), false, matrix);
        unbind();
    }

    /**
     * Pass a uniform int value into the shader.
     *
     * @param name  Name of the uniform
     * @param value Value of the uniform
     */
    public void uniformValue1i(String name, int value) {
        bind();
        glUniform1i(glGetUniformLocation(id, name), value);
        unbind();
    }
}
