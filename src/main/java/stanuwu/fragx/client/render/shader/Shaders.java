package stanuwu.fragx.client.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GLImportProcessor;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL33.*;

/**
 * Class responsible for storing and creating shaders.
 */
@UtilityClass
public class Shaders {
    @Getter
    private Shader positionColorShader;
    @Getter
    private Shader roundedRectPositionColorShader;
    @Getter
    private Shader positionColorTextureShader;
    @Getter
    private Shader positionTextureShader;

    /**
     * Set up shaders to load once the client starts.
     */
    public void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> loadShaders());
    }

    /**
     * Create all shaders.
     */
    private void loadShaders() {
        positionColorShader = new Shader("position_color");
        roundedRectPositionColorShader = new Shader("rounded_rect_position_color");
        positionColorTextureShader = new Shader("position_color_texture");
        positionTextureShader = new Shader("position_texture");
    }

    /**
     * Load a shader from its file into opengl
     *
     * @param name Filename of the shader
     * @param type Type of the shader
     * @return ID of the shader
     */
    public int loadShaderProgram(String name, ShaderType type) {
        try {
            ResourceFactory resourceFactory = MinecraftClient.getInstance().getResourceManager();
            Resource resource = resourceFactory.getResource(new Identifier("fragx", "shader/" + name + type.fileExtension));
            int i = glCreateShader(type.glType);
            GlStateManager.glShaderSource(i, new GLImportProcessor() {
                @SneakyThrows
                @Nullable
                @Override
                public String loadImport(boolean inline, String name) {
                    return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
                }
            }.readSource(TextureUtil.readResourceAsString(resource.getInputStream())));
            glCompileShader(i);
            if (glGetShaderi(i, 35713) == 0) {
                String shaderInfo = StringUtils.trim(glGetShaderInfoLog(i, 32768));
                throw new IOException("Couldn't compile " + type.name + " program (" + name + ") : " + shaderInfo);
            }
            return i;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Holds information about different types of shaders.
     */
    public enum ShaderType {
        VERTEX("vertex", ".vsh", GL_VERTEX_SHADER),
        FRAGMENT("fragment", ".fsh", GL_FRAGMENT_SHADER);

        private final String name;
        private final String fileExtension;
        private final int glType;

        /**
         * @param name      Name of the shader type
         * @param extension File extension of the shader type
         * @param glType    GL enum of the shader type
         */
        ShaderType(String name, String extension, int glType) {
            this.name = name;
            this.fileExtension = extension;
            this.glType = glType;
        }
    }
}
