package plugins.FreePublisher.models;

import freenet.client.HighLevelSimpleClient;
import freenet.pluginmanager.PluginRespirator;

/**
 *
 * @author zapu
 */
public abstract class FreenetModel
{
    public FreenetModel(PluginRespirator respirator)
    {
        this.respirator = respirator;
    }

    private PluginRespirator respirator;

    //lets see if we can deal without exposing respirator
    /*protected PluginRespirator getRespirator()
    {
        return respirator;
    }*/

    protected HighLevelSimpleClient getHLSL()
    {
        return respirator.getHLSimpleClient();
    }
}
