package plugins.FreePublisher.models;

import freenet.client.ClientMetadata;
import freenet.client.FetchResult;
import freenet.client.InsertBlock;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.io.ArrayBucket;
import freenet.support.io.Closer;
import java.io.IOException;
import java.io.OutputStream;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.events.EventTable;

/**
 *
 * @author zapu
 */
public class EventTableModel extends FreenetModel
{
    public EventTableModel(PluginRespirator respirator)
    {
        super(respirator);
    }

    public void insertEventTable(Identity identity, EventTable eventTable) throws Exception
    {
        System.err.println("insertEventTable started");

        ArrayBucket arrayBucket = new ArrayBucket("eventTableBucket");
        OutputStream eventStream = arrayBucket.getOutputStream();
        eventTable.outputEventTable(eventStream);
        eventStream.flush();
        Closer.close(eventStream);

        System.err.println("EventTable serialized. Inserting " + arrayBucket.size() + " bytes");

        getHLSL().insert(new InsertBlock(arrayBucket, new ClientMetadata("text/plain"), new FreenetURI("USK@" + identity.getPrivateKey() + "/events.xml/0")), false, null);

        System.err.println("Inserted EventTable. Success!");
    }

    public EventTable fetchEventTable(String key) throws Exception
    {
        System.err.println("FetchEventTable started");

        FetchResult fetch = getHLSL().fetch(new FreenetURI("USK@" + key + "/events.xml/0"));

        System.err.println("Fetched event table.");

        EventTable table = new EventTable();
        table.loadEventTable(fetch.asBucket().getInputStream());

        System.err.println("Event table parsed. Success");

        return table;
    }
}
