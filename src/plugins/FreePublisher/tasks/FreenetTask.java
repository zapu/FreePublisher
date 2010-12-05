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
    }

    private boolean taskDone;
    private int percentDone;
    private int taskId;
    private boolean taskStarted;
    private Thread thread;

    public void run()
    {
        taskStarted = true;
        taskRun();
        taskDone = true;
    }

    public abstract void taskStatus(HTMLNode contentNode);

    public abstract void taskRun();

    public synchronized boolean isTaskStarted() { return taskStarted; }
    public synchronized boolean isTaskDone() { return taskDone; }

    public void runBackground()
    {
        thread = new Thread(this);
        thread.start();
    }
}
