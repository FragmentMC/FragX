package stanuwu.fragx.client.event;


import stanuwu.fragx.client.event.events.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * EventHandler for registering and firing events.
 */
public class EventHandler {
    private final HashMap<EventType, List<Consumer<Event>>> EVENTS = new HashMap<>();

    private static final EventHandler INSTANCE = new EventHandler();

    /**
     * @return Current EventHandler
     */
    public static EventHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Subscribe to an event with a function.
     *
     * @param eventType The type of event to subscribe to
     * @param function  The function to hook to the event
     */
    public void register(EventType eventType, Consumer<Event> function) {
        EVENTS.putIfAbsent(eventType, new ArrayList<>());
        EVENTS.get(eventType).add(function);
    }

    /**
     * Fire an event.
     *
     * @param eventType The type of event to fire
     * @param event     The event object to include
     */
    public void fire(EventType eventType, Event event) {
        if (EVENTS.containsKey(eventType)) {
            EVENTS.get(eventType).forEach(consumer -> consumer.accept(event));
        }
    }
}