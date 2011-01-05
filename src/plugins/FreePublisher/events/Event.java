package plugins.FreePublisher.events;

import java.util.Date;
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

    public Date date;

    public final Element getElement()
    {
        Element element = new Element("event");
        element.setAttribute("type", String.valueOf(getEventType().getId()));
        element.setAttribute("date", String.valueOf(date.getTime()));
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
        event.date = new Date(Long.parseLong(element.getAttribute("date").getValue()));
        event.unserialize(element);
        return event;
    }
}
