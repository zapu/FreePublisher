package plugins.FreePublisher.tasks;

import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;

/**
 *
 * @author zapu
 */
public abstract class FreenetTask
{
    public FreenetTask()
    {
        taskDone = false;
        percentDone = 0;
    }

    private boolean taskDone;
    private int percentDone;
    private int taskId;

    public abstract void run();
    public abstract void taskStatus(HTMLNode contentNode);
}
