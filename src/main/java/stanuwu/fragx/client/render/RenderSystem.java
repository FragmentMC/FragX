package stanuwu.fragx.client.render;

import lombok.experimental.UtilityClass;
import stanuwu.fragx.client.render.buffer.BufferRenderer;
import stanuwu.fragx.client.render.font.TTFFontManager;
import stanuwu.fragx.client.render.scale.ScaleHelper;
import stanuwu.fragx.client.render.shader.Shaders;

/**
 * Class for managing all parts of the render system.
 */
@UtilityClass
public class RenderSystem {
    /**
     * Initialize all parts of the render system.
     */
    public void init() {
        BufferRenderer.init();
        Shaders.init();
        ScaleHelper.init();
        TTFFontManager.init();
    }
}
