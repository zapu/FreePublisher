package plugins.FreePublisher.events;

import com.db4o.foundation.NotSupportedException;
import org.jdom.*;

/**
 *
 * @author zapu
 */
public abstract class Event
{
    public abstract EventType getEventType();

    public void unserialize(Element element)
    {

    }

    public Element serialize()
    {
        throw new NotSupportedException();
    }

    public static Event getEvent(Element element) throws Exception
    {
        int eventTypeId = Integer.parseInt(element.getAttribute("type").getValue());
        EventType type = EventType.fromId(eventTypeId);
        if(type == null)
            return null;

        Event event = type.createEvent();
        event.unserialize(element);
        return event;
    }
}
