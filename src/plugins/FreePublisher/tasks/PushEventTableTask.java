package plugins.FreePublisher.tasks;

import freenet.client.HighLevelSimpleClient;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.events.EventTable;

/**
 *
 * @author zapu
 */
public class PushEventTableTask implements Runnable
{
    private Identity identity;
    private EventTable eventTable;
    private HighLevelSimpleClient HLSL;

    PushEventTableTask(HighLevelSimpleClient HLSL, Identity identity, EventTable eventTable)
    {
        this.identity = identity;
        this.eventTable = eventTable;
        this.HLSL = HLSL;
    }

    public synchronized void run()
    {
        
        this.notifyAll();
    }
}
