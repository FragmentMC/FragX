package stanuwu.fragx.client.event.events;

import lombok.Getter;
import net.minecraft.client.util.Window;

/**
 * Event fired when the size of the game window changes.
 */
public class ResizeWindowEvent extends Event {
    @Getter
    private final Window window;

    /**
     * @param window Window object
     */
    public ResizeWindowEvent(Window window) {
        this.window = window;
    }
}
