package plugins.FreePublisher.ui;

import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import plugins.FreePublisher.Publisher;
import plugins.FreePublisher.models.EventTableModel;

/**
 *
 * @author zapu
 */
public class InsertPage extends WebPage
{
    private EventTableModel eventTableModel;

    public InsertPage(Publisher publisher)
    {
        super(publisher);

        eventTableModel = getPublisher().getModel(EventTableModel.class);
    }

    @Override
    public String path()
    {
        return "/publisher/insert";
    }

    class IndexAction implements WebPageAction
    {
        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class InsertAction implements WebPageAction, Runnable
    {
        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void run()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
