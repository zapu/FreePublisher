package plugins.FreePublisher.ui;

import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import plugins.FreePublisher.Publisher;
import plugins.FreePublisher.models.EventTableModel;

/**
 *
 * @author zapu
 */
public class InsertPage extends Controller
{
    private EventTableModel eventTableModel;

    public InsertPage(Publisher publisher)
    {
        super(publisher);

        eventTableModel = getPublisher().getModel(EventTableModel.class);

        registerAction("", new IndexAction());
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
            return STATUS_NOERROR;
        }
    }

    class InsertAction implements WebPageAction, Runnable
    {
        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            return STATUS_NOERROR;
        }

        public void run()
        {
            
        }
    }

    class AddInsertAction implements WebPageAction
    {
        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            if(!post)
                return STATUS_ERROR;

            if(!request.isPartSet("uploadType"))
                return STATUS_ERROR;

            String uploadType = request.getPartAsStringFailsafe("uploadType", 10);
            if(uploadType.equals("httpupload"))
            {

            }
            else if (uploadType.equals("filename"))
            {

            }
            else
                return STATUS_ERROR;

            return STATUS_NOERROR;
        }
    }
}
