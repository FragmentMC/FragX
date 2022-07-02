package stanuwu.fragx.client.render.font;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.util.ArrayList;
import java.util.HashMap;

@UtilityClass
/**
 * Load and manage TTFFontRenderer instances.
 */
public class TTFFontManager {
    private boolean initialized;
    private final HashMap<String, TTFFontRenderer> fonts = new HashMap<>();
    private final ArrayList<FontData> queue = new ArrayList<>();

    /**
     * Instantiates all font renderers that were registered before, and sets new fonts to register instantly. Call this once fonts can be created.
     */
    public void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client -> {
            for (FontData font : queue) {
                fonts.put(font.name + font.size, new TTFFontRenderer(font.name, font.size));
            }
            initialized = true;
        }));
    }

    /**
     * Generate and register a new TTFFontRenderer for the specified font.
     *
     * @param name Name of the font
     * @param size Size of the font
     */
    public void addFont(String name, int size) {
        if (initialized) {
            fonts.put(name + size, new TTFFontRenderer(name, size));
        } else {
            queue.add(new FontData(name, size));
        }
    }

    /**
     * Get a TTFFontRenderer for the specified font.
     *
     * @param name Name of the font
     * @param size Size of the font
     * @return TTFFontRenderer for the specified font
     */
    public TTFFontRenderer getFont(String name, int size) {
        return fonts.get(name + size);
    }

    /**
     * Holds data for fonts that are yet to be loaded.
     */
    private record FontData(String name, int size) {
    }
}