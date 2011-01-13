package plugins.FreePublisher;

import freenet.client.ClientMetadata;
import freenet.client.HighLevelSimpleClient;
import freenet.client.InsertBlock;
import freenet.keys.FreenetURI;
import freenet.support.api.Bucket;
import freenet.support.io.ArrayBucket;
import freenet.support.io.Closer;
import java.io.OutputStream;
import java.util.Date;

/**
 *
 * @author zapu
 */
public class UpdateTableJob implements Runnable
{
    public UpdateTableJob(Publisher publisher)
    {
        this.publisher = publisher;
    }

    private final Publisher publisher;

    static public final int Interval = 10 * 60 * 1000;
    private boolean terminated = false;
    private boolean working = false;
    private boolean forced = false;

    public void run()
    {
        while(!terminated)
        {
            if(!forced)
            {
                synchronized(this)
                {
                    try
                    {
                        wait(Interval);
                    }
                    catch(InterruptedException e)
                    {

                    }
                }
            }

            forced = false;

            if(terminated)
                return;

            working = true;
            work();
            working = false;
        }
    }

    public synchronized void forceRun()
    {
        forced = true;
        notifyAll();
    }

    public synchronized void terminate()
    {
        terminated = true;
        notifyAll();
    }

    public boolean isWorking()
    {
        return working;
    }

    public boolean isForced()
    {
        return forced;
    }

    private void work()
    {
        publisher.identityLock.lock();

        if(publisher.eventTable == null || publisher.identity == null)
        {
            System.err.println("UpdateTableJob: nothing to do (identity not loaded) " + new Date());
            publisher.identityLock.unlock();
            return;
        }

        if(!publisher.eventTable.isDirty())
        {
            System.err.println("UpdateTableJob: nothing to do (eventTable not dirty) " + new Date());
            publisher.identityLock.unlock();
            return;
        }

        FreenetURI tableURI;
        ArrayBucket bucket = new ArrayBucket("EventTableBucket_Update");

        System.err.println("UpdateTableJob: Pushing event table...");

        try
        {
            tableURI = new FreenetURI("USK@" + publisher.identity.getPrivateKey() + "/events.xml/0");

            OutputStream stream = bucket.getOutputStream();
            publisher.eventTable.outputEventTable(stream);
            stream.flush();
            Closer.close(stream);
        }
        catch(Exception e)
        {
            System.err.println("UpdateTableJob: Error (preparing): " + e);
            return;
        }
        finally
        {
            publisher.identityLock.unlock();
        }

        HighLevelSimpleClient HLSL = publisher.getPR().getHLSimpleClient();

        try
        {
            System.err.println("UpdateTableJob: inserting " + bucket.size() + " bytes");
            HLSL.insert(new InsertBlock(bucket, new ClientMetadata("text/plain"), tableURI), false, null);
        }
        catch(Exception e)
        {
            System.err.println("UpdateTableJob: Error (inserting): " + e);
        }

        System.err.println("UpdateTableJob: done " + new Date());
    }
}
