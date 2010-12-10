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

    public abstract void unserialize(Element element);

    public abstract void serialize(Element element);

    public final Element getElement()
    {
        Element element = new Element("event");
        element.setAttribute("type", String.valueOf(getEventType().getId()));
        serialize(element);
        return element;
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
