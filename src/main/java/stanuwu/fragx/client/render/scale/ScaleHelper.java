package stanuwu.fragx.client.render.scale;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.util.Window;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import stanuwu.fragx.client.event.Events;

import java.nio.FloatBuffer;

/**
 * Class to help with Minecraft gui scaling
 */
@UtilityClass
public class ScaleHelper {
    @Getter
    private FloatBuffer projectionMatrix;
    @Getter
    private float scale;

    /**
     * Hook resize window event and generate initial data
     */
    public void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> scaleChange(client.getWindow()));
        Events.RESIZE_WINDOW.register(event -> scaleChange(event.getWindow()));
    }

    /**
     * Generate a new scaled projection matrix and set the scale, when the window size changes.
     *
     * @param window Window object
     */
    private void scaleChange(Window window) {
        scale = (float) window.getScaleFactor();
        projectionMatrix = new Matrix4f()
                .setOrtho(0, window.getScaledWidth(), 0, window.getScaledHeight(), -1, 1)
                .get(BufferUtils.createFloatBuffer(16));
    }
}
