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

    static public final int Interval = 2 * 60 * 1000;
    private boolean terminated = false;
    private boolean working = false;

    public synchronized void run()
    {
        System.err.println("UpdateTableJob starts...");

        while(!terminated)
        {
            try
            {
                wait(Interval);
            }
            catch(InterruptedException e)
            {
                
            }

            if(terminated)
                return;

            System.err.println("Called");

            working = true;
            work();
            working = false;
        }
    }

    public synchronized void forceRun()
    {
        notifyAll();
    }

    public synchronized void terminate()
    {
        terminated = true;
        notifyAll();
    }

    public synchronized boolean isWorking()
    {
        return working;
    }

    private synchronized void work()
    {
        publisher.identityLock.lock();

        try
        {

        }
        finally
        {
            publisher.identityLock.unlock();
        }
    }
}
