package plugins.FreePublisher;

import freenet.pluginmanager.PluginRespirator;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import plugins.FreePublisher.events.EventTable;
import plugins.FreePublisher.models.*;

/**
 *
 * @author zapu
 */
public class Publisher
{
    public Publisher(PluginRespirator pr)
    {
        respirator = pr;
        
        models = new HashMap<Class, FreenetModel>();
        models.put(IdentityModel.class, new IdentityModel(respirator));
        models.put(EventTableModel.class, new EventTableModel(respirator));
        models.put(DataModel.class, new DataModel(respirator));

        updateJob = new UpdateTableJob(this);
    }

    PluginRespirator respirator;
    Map<Class, FreenetModel> models;

    public Identity identity;
    public EventTable eventTable;

    public final ReentrantLock identityLock = new ReentrantLock();

    private UpdateTableJob updateJob;

    public <T> T getModel(Class c)
    {
        return (T) models.get(c);
    }

    public PluginRespirator getPR()
    {
        return respirator;
    }

    public void init()
    {
        new Thread(updateJob).start();
    }

    public void deinit()
    {
        updateJob.terminate();
    }
}
