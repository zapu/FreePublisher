package plugins.FreePublisher.events;

import freenet.support.HTMLNode;
import org.jdom.Element;

/**
 *
 * @author zapu
 */
public class EventAdd extends Event
{
    @Override
    public EventType getEventType()
    {
        return EventType.ADD;
    }

    public String title;
    public String getURI;

    @Override
    public void unserialize(Element element)
    {
        Element elem = element.getChild("title");
        if(elem == null)
            return;
        title = elem.getTextTrim();
        elem = element.getChild("getURI");
        if(elem == null)
            return;
        getURI = elem.getTextTrim();
    }

    @Override
    public void serialize(Element element)
    {
        element.getChildren().add(new Element("title").setText(title));
        element.getChildren().add(new Element("getURI").setText(getURI));
    }

    @Override
    public HTMLNode getHTMLNode()
    {
        return new HTMLNode("a",
                new String[] { "href" },
                new String[] { "http://127.0.0.1:8888/" + getURI },
                title
        );
    }
}
