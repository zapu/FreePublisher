package plugins.FreePublisher.tasks;

import freenet.client.FetchResult;
import freenet.keys.FreenetURI;
import freenet.support.HTMLNode;
import java.io.ByteArrayInputStream;
import java.io.File;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.events.EventTable;

/**
 *
 * @author zapu
 */
public class LoadIdentityTask extends FreenetTask
{
    private String filename;

    public LoadIdentityTask(String filename)
    {
        this.filename = filename;
    }

    @Override
    public void taskStatus(HTMLNode contentNode)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String status;

    @Override
    public void taskRun()
    {
        try
        {
            File file = new File(filename);
            Identity identity = new Identity(file);

            status = "Fetching EventTable.";

            FetchResult result = FreePublisher.getInstance().getPR().getHLSimpleClient().
                    fetch(new FreenetURI(identity.getPublicKey()));

            EventTable eventTable = new EventTable();
            eventTable.loadEventTable(new ByteArrayInputStream(result.asByteArray()));

            FreePublisher.getInstance().identity = identity;
            FreePublisher.getInstance().eventTable = eventTable;
        }
        catch(Exception e)
        {
            status = "Failed";
        }

        status = "Loaded.";
    }

}
