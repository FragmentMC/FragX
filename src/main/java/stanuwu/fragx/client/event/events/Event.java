package stanuwu.fragx.client.event.events;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class of an event for the event system.
 */
public class Event {
    @Setter
    @Getter
    private boolean cancelled;

    /**
     * Create a new event.
     */
    public Event() {
        this.cancelled = false;
    }
}
