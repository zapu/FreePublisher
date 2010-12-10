package plugins.FreePublisher.events;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author zapu
 */
public class EventTable
{
    public EventTable()
    {
        events = new LinkedList();
    }

    public List<Event> events;

    public void loadEventTable(InputStream stream) throws JDOMException, IOException, Exception
    {
        SAXBuilder builder = new SAXBuilder();
        //builder.setValidation(true);
        builder.setIgnoringElementContentWhitespace(true);
        Document doc = builder.build(stream);

        Element root = doc.getRootElement();
        Element events = root.getChild("events");

        for(Object obj : events.getChildren())
        {
            Element eventElement = (Element) obj;
            Event event = Event.getEvent(eventElement);
            if(event != null)
            {
                this.events.add(event);
            }
        }
    }
}
