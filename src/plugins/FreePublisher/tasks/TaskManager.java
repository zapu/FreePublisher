package plugins.FreePublisher.tasks;

import freenet.client.FetchContext;
import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.client.HighLevelSimpleClient;
import freenet.client.InsertBlock;
import freenet.client.InsertContext;
import freenet.client.InsertException;
import freenet.client.async.ClientGetCallback;
import freenet.client.async.ClientGetter;
import freenet.client.async.ClientPutCallback;
import freenet.client.async.ClientPutter;
import freenet.client.events.ClientEventListener;
import freenet.keys.FreenetURI;
import freenet.node.RequestClient;
import freenet.support.HTMLNode;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import plugins.FreePublisher.ui.WebPage;
import java.util.Map;

/**
 *
 * @author zapu
 */
public class TaskManager extends WebPage
{
    public TaskManager(HighLevelSimpleClient hlsc)
    {
        super(hlsc);

        taskList = new HashMap<Integer, FreenetTask>();
        taskAutoId = 0;
    }

    private Map<Integer, FreenetTask> taskList;
    private int taskAutoId;

    public void addTask(FreenetTask task)
    {
        taskList.put(taskAutoId++, task);
        task.runBackground();
    }

    public void removeTask(int id)
    {
        FreenetTask task = taskList.get(id);
        if(task == null)
            return;
    }
}
