package stanuwu.fragx.client.event;

import stanuwu.fragx.client.event.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class responsible for registering and firing events.
 *
 * @param <T> Type of the Event that is being handled.
 */
public class EventHandler<T extends Event> {
    private final List<Consumer<T>> events;

    /**
     * Instantiate an empty EventHandler.
     */
    EventHandler() {
        this.events = new ArrayList<>();
    }

    /**
     * Subscribe to the event.
     *
     * @param subscriber Method to subscribe to the event.
     */
    public void register(Consumer<T> subscriber) {
        this.events.add(subscriber);
    }

    /**
     * Trigger the event.
     *
     * @param event Event object to pass.
     */
    public void fire(T event) {
        for (Consumer<T> subscriber : this.events) {
            subscriber.accept(event);
        }
    }
}