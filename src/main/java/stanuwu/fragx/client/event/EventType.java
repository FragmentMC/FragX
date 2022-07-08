package stanuwu.fragx.client.event;

import stanuwu.fragx.client.event.events.Event;
import stanuwu.fragx.client.event.events.ResizeWindowEvent;

/**
 * Event Types
 */
public enum EventType {
    RESIZE_WINDOW(ResizeWindowEvent.class);

    public final Class<? extends Event> event;

    /**
     * @param event Class of the event object for this event type
     */
    EventType(Class<? extends Event> event) {
        this.event = event;
    }
}
