/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins.FreePublisher.events;

import java.lang.reflect.Constructor;

/**
 *
 * @author zapu
 */
public enum EventType
{
    ADD(0, EventAdd.class);

    private final Class eventClass;
    private final int eventId;
    EventType(int id, Class cl)
    {
        eventId = id;
        eventClass = cl;
    }

    public int getId()
    {
        return eventId;
    }

    public Event createEvent() throws Exception
    {
        Constructor constructor = eventClass.getConstructor();
        return (Event) constructor.newInstance();
    }

    public static EventType fromId(int id)
    {
        for(EventType type : EventType.values())
        {
            if(type.getId() == id)
                return type;
        }

        return null;
    }
}
