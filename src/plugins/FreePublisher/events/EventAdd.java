package plugins.FreePublisher.events;

/**
 *
 * @author zapu
 */
public class EventAdd extends Event
{

    @Override
    public EventType getEventType() {
        return EventType.ADD;
    }

}
