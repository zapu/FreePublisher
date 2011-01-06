package plugins.FreePublisher;

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

    public synchronized void run()
    {
        System.err.println("UpdateTableJob starts...");

        while(!terminated)
        {
            if(!forced)
            {
                try
                {
                    wait(Interval);
                }
                catch(InterruptedException e)
                {

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

    private synchronized void work()
    {
        publisher.identityLock.lock();

        try
        {
            Thread.sleep(2000);
        }
        catch(Exception e)
        {

        }
        finally
        {
            publisher.identityLock.unlock();
        }
    }
}
