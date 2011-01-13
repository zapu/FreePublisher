package plugins.FreePublisher.ui;

import freenet.client.ClientMetadata;
import freenet.client.InsertException;
import freenet.keys.FreenetURI;
import freenet.support.HTMLNode;
import freenet.support.api.Bucket;
import freenet.support.api.HTTPRequest;
import freenet.support.api.HTTPUploadedFile;
import freenet.support.io.ArrayBucket;
import freenet.support.io.Closer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import plugins.FreePublisher.Publisher;
import plugins.FreePublisher.events.EventAdd;
import plugins.FreePublisher.models.DataModel;
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
        registerAction("insert", new AddInsertAction());
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
            if(getPublisher().identity == null)
            {
                contentNode.addChild("h1", "Identity is not loaded.");
                contentNode.addChild("h2", "Load identity first.");

                return STATUS_NOERROR;
            }

            contentNode.addChild("h2", "New insert");

            HTMLNode form = getPR().addFormChild(contentNode, "", "form name");

            form.addChild("span", "Title:");
            form.addChild("br");
            form.addChild("input",
                            new String[] { "class", "type", "name" },
                            new String[] { "config", "text", "title" });
            form.addChild("br");

            form.addChild("span", "File upload via browser (slower):");
            form.addChild("br");
            form.addChild("input",
                            new String[] { "class", "type", "name" },
                            new String[] { "config", "file", "uploadedFile" });

            form.addChild("br");
            form.addChild("span", "Upload filename:");
            form.addChild("br");
            form.addChild("input",
                            new String[] { "class", "type", "name" },
                            new String[] { "config", "text", "uploadFilename" });

            form.addChild("input",
                            new String[] { "type", "name", "value" },
                            new String[] { "hidden", "action", "insert" });

            form.addChild("br");
            form.addChild("input",
                            new String[] { "type", "value", "style" },
                            new String[] { "submit", "Insert", "margin-top:10px; margin-bottom:20px;" });

            if(!insertJobs.isEmpty())
            {
                contentNode.addChild("h2", "Inserts pending");
                HTMLNode list = contentNode.addChild("ul");
                for(InsertJob job : insertJobs)
                {
                    list.addChild("li").addChild("span", job.toString());
                }
            }

            return STATUS_NOERROR;
        }
    }

    class InsertJob implements Runnable
    {
        private boolean cancel;
        private boolean done;
        private Bucket bucket;
        private ClientMetadata meta;
        private FreenetURI uri;
        private String title;

        private String lastError;

        public InsertJob(String title, Bucket bucket, ClientMetadata meta, FreenetURI uri)
        {
            this.title = title;
            this.bucket = bucket;
            this.meta = meta;
            this.uri = uri;

            cancel = false;

            lastError = "";
        }

        public void setCancel()
        {
            cancel = true;
        }

        public boolean isDone()
        {
            return done;
        }

        public void run()
        {
            DataModel model = getPublisher().getModel(DataModel.class);
            try
            {
                FreenetURI insertedUri = model.insertBucket(bucket, meta, uri);

                EventAdd newEvent = new EventAdd();
                newEvent.date = new Date();
                newEvent.title = title;
                newEvent.getURI = insertedUri.toString();

                if(cancel)
                    return;

                try
                {
                    getPublisher().identityLock.lock();
                    getPublisher().eventTable.getEvents().add(newEvent);
                    getPublisher().eventTable.setDirty(true);
                }
                finally
                {
                    getPublisher().identityLock.unlock();
                }

                done = true;
            }
            catch (Exception ex)
            {
                System.err.println(ex.toString());
                lastError = ex.getMessage();
            }
        }

        @Override
        public String toString()
        {
            return title + "(" + (done ? "done" : "working") + ")";
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

            if(getPublisher().identity == null)
            {
                contentNode.addChild("p", "Identity not loaded.");
                return STATUS_ERROR;
            }

            Bucket bucket = null;
            ClientMetadata metadata = new ClientMetadata("text/plain");
            String uploadFilename = request.getPartAsStringFailsafe("uploadFilename", 128);
            if(uploadFilename.isEmpty())
            {
                HTTPUploadedFile uploadedFile = request.getUploadedFile("uploadedFile");
                if(uploadedFile == null)
                {
                    System.err.println("blahblah");
                }
                else
                {
                    System.err.println("Uploading from browser");
                    ArrayBucket ab = new ArrayBucket("insertdata");
                    try
                    {
                        OutputStream outputStream = ab.getOutputStream();

                        InputStream inputStream = uploadedFile.getData().getInputStream();
                        for(int i = 0; i < uploadedFile.getData().size(); i++)
                            outputStream.write(inputStream.read());
                        
                        outputStream.flush();
                        Closer.close(outputStream);
                    }
                    catch (IOException ex)
                    {
                        System.err.println("Bucket copying exception: " + ex);
                        return STATUS_ERROR;
                    }

                    bucket = ab;

                    metadata = new ClientMetadata(uploadedFile.getContentType());
                }
            }
            
            String title = request.getPartAsStringFailsafe("title", 64);

            InsertJob action = new InsertJob(title, bucket, metadata, null);
            insertJobs.add(action);
            new Thread(action).start();

            contentNode.addChild("p", "Insert dispatched.");
            contentNode.addChild("br");
            contentNode.addChild("a",
                new String[] { "href" },
                new String[] { path() },
                "back");

            return STATUS_NOERROR;
        }
    }
}
