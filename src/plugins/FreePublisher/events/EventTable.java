package plugins.FreePublisher.events;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        dirty = false;
    }

    private List<Event> events;
    private String info = "";

    public void loadEventTable(InputStream stream) throws JDOMException, IOException, Exception
    {
        SAXBuilder builder = new SAXBuilder();
        //builder.setValidation(true);
        builder.setIgnoringElementContentWhitespace(true);
        Document doc = builder.build(stream);

        Element root = doc.getRootElement();

        Element infoElement = root.getChild("info");
        if(infoElement != null)
        {
            info = infoElement.getTextTrim();
        }

        Element eventsElement = root.getChild("events");
        if(eventsElement != null)
        {
            for(Object obj : eventsElement.getChildren())
            {
                Element eventElement = (Element) obj;
                Event event = Event.getEvent(eventElement);
                if(event != null)
                {
                    events.add(event);
                }
            }
        }
    }

    public void outputEventTable(OutputStream stream) throws IOException
    {
        Document doc = new Document();
        Element root = new Element("eventTable");

        Element eventsElement = new Element("events");
        for(Event event : events)
        {
            eventsElement.getChildren().add(event.getElement());
        }
        root.getChildren().add(eventsElement);

        if(!info.isEmpty())
        {
            root.getChildren().add(new Element("info").setText(info));
        }

        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, stream);
    }

    public List<Event> getEvents()
    {
        return events;
    }

    private boolean dirty;
    public boolean isDirty() { return dirty; }
    public void setDirty(boolean val) { dirty = val; }
}
