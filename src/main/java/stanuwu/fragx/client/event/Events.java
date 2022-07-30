package stanuwu.fragx.client.event;

import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import stanuwu.fragx.client.event.events.ResizeWindowEvent;

/**
 * Holds all the events.
 */
@UtilityClass
@FieldDefaults(makeFinal = true)
public class Events {
    public EventHandler<ResizeWindowEvent> RESIZE_WINDOW = new EventHandler<>();
}
