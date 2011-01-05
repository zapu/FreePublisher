package plugins.FreePublisher.ui;

import freenet.support.HTMLNode;
import freenet.support.api.Bucket;
import freenet.support.api.HTTPRequest;
import freenet.support.api.HTTPUploadedFile;
import freenet.support.io.ArrayBucket;
import java.util.LinkedList;
import java.util.List;
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

        insertJobs = new LinkedList<InsertJob>();

        registerAction("", new IndexAction());
    }

    @Override
    public String path()
    {
        return "/publisher/insert";
    }

    List<InsertJob> insertJobs;

    class IndexAction implements WebPageAction
    {
        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {


            contentNode.addChild("h2", "New insert");

            if(!insertJobs.isEmpty())
            {
                contentNode.addChild("h2", "Inserts pending");
                HTMLNode list = contentNode.addChild("ul");
                for(InsertJob job : insertJobs)
                {
                    list.addChild("li").addChild("a",
                            new String [] { "href" } ,
                            new String [] { "" },
                            job.toString());
                }
            }

            return STATUS_NOERROR;
        }
    }

    class InsertJob implements Runnable
    {
        private boolean cancel;

        public void setCancel()
        {
            cancel = true;
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
            {
                return STATUS_ERROR;
            }

            if(!request.isPartSet("uploadType"))
            {
                return STATUS_ERROR;
            }

            if(getPublisher().identity == null)
            {
                return STATUS_ERROR;
            }

            Bucket bucket;
            String uploadType = request.getPartAsStringFailsafe("uploadType", 10);
            if(uploadType.equals("httpupload"))
            {
                HTTPUploadedFile uploadedFile = request.getUploadedFile("uploadedFile");
                bucket = uploadedFile.getData();
            }
            else if (uploadType.equals("filename"))
            {
                bucket = new ArrayBucket("uploadBucket");;
            }
            else
                return STATUS_ERROR;

            InsertJob action = new InsertJob();
            insertJobs.add(action);

            return STATUS_NOERROR;
        }
    }
}
