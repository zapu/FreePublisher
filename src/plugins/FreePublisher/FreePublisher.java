package plugins.FreePublisher;

import freenet.client.ClientMetadata;
import freenet.client.HighLevelSimpleClient;
import freenet.client.InsertBlock;
import freenet.keys.FreenetURI;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import plugins.FreePublisher.ui.UserInterface;
import freenet.l10n.BaseL10n.LANGUAGE;
import freenet.node.Version;
import freenet.pluginmanager.*;
import freenet.support.Logger;
import freenet.support.api.Bucket;
import freenet.support.io.ArrayBucket;
import freenet.support.io.ArrayBucketFactory;
import freenet.support.io.Closer;
import plugins.FreePublisher.events.EventTable;
import plugins.FreePublisher.tasks.TaskManager;

/**
 *
 * @author zapu
 */
public class FreePublisher implements FredPlugin, FredPluginThreadless, FredPluginL10n
{
    private static FreePublisher instance;
    public static synchronized FreePublisher getInstance()
    {
        return instance;
    }

    private UserInterface userInterface;
    private PluginRespirator respirator;

    private Identity identity;
    private EventTable eventTable;

    public TaskManager taskManager;

    public FreePublisher()
    {
        instance = this;
        identity = null;
    }

    public void runPlugin(PluginRespirator pr)
    {
        System.err.println("FreePublisher started.");
        
        respirator = pr;

        taskManager = new TaskManager(pr.getHLSimpleClient());

        userInterface = new UserInterface(pr);
        userInterface.load();

        /*
        TODO: Remove this, or keep as reference on how to insert something to Freenet :)

        try
        {
            HighLevelSimpleClient hlsl = pr.getHLSimpleClient();
            System.err.println("Testing insert. Generating URI.");
            FreenetURI[] uri = hlsl.generateKeyPair("");
            System.err.println("Generated URI:");
            System.err.println("Insert URI: " + uri[0].toString());
            System.err.println("Request URI: " + uri[1].toString());
            System.err.println("Generating bucket.");
            ArrayBucket arrb = new ArrayBucket("bucket");
            OutputStream outputStream = arrb.getOutputStream();
            outputStream.write(new String("this is a test").getBytes());
            outputStream.flush();
            Closer.close(outputStream);
            System.err.println("bucket size: " + arrb.size());

            hlsl.insert(new InsertBlock(arrb, new ClientMetadata("text/plain"), new FreenetURI(uri[0] + "test.txt")), false, null);
            System.err.println("done");
        }
        catch (Exception ex)
        {
            System.err.println("EXCEPTION: " + ex.toString());
        }
*/
    }

    public void terminate()
    {
        System.err.println("FreePublisher terminated.");

        userInterface.unload();
    }


    public String getVersion()
    {
        return "0.1alpha";
    }

    public long getRealVersion()
    {
        return 1;
    }

    public void setLanguage(LANGUAGE lng)
    {
        //not supported yet, ignore silently
    }

    public String getString(String string)
    {
        //not supported, just return key
        return string;
    }

    public PluginRespirator getPR()
    {
        return respirator;
    }

    public Identity getIdentity()
    {
        return identity;
    }

    public EventTable getEventTable()
    {
        return eventTable;
    }

    public void setIdentity(Identity identity, EventTable table)
    {
        this.identity = identity;
        this.eventTable = table;
    }

}
