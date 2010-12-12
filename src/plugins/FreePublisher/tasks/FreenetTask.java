package plugins.FreePublisher.tasks;

import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import java.lang.Thread;

/**
 *
 * @author zapu
 */
public abstract class FreenetTask implements Runnable
{
    public FreenetTask()
    {
        taskDone = false;
        taskStarted = false;
        percentDone = 0;
        status = Status.Waiting;
    }

    enum Status
    {
        Waiting,
        Started,
        Done,
        Error,
    }

    private Status status;

    private boolean taskDone;
    private int percentDone;
    private int taskId;
    private boolean taskStarted;
    private Thread thread;

    public void run()
    {
        status = Status.Started;
        taskRun();
        status = Status.Done;
    }

    public abstract void taskStatus(HTMLNode contentNode);

    public abstract void taskRun();

    public void runBackground()
    {
        thread = new Thread(this);
        thread.start();
    }

    protected void notifyError()
    {
        status = Status.Error;
    }
}
