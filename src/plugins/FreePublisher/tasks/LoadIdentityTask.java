package plugins.FreePublisher.tasks;

import freenet.client.FetchResult;
import freenet.keys.FreenetURI;
import freenet.support.HTMLNode;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.events.EventTable;

/**
 *
 * @author zapu
 */
public class LoadIdentityTask extends FreenetTask
{
    private File file;

    public LoadIdentityTask(File file)
    {
        this.file = file;
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
            Identity identity = new Identity(new FileInputStream(file));

            status = "Fetching EventTable.";

            FetchResult result = FreePublisher.getInstance().getPR().getHLSimpleClient().
                    fetch(new FreenetURI("USK@" + identity.getPublicKey() + "/table.xml/0"));

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
