package plugins.FreePublisher;

import freenet.pluginmanager.PluginRespirator;

/**
 *
 * @author zapu
 */
public class MockFreePublisher extends FreePublisher
{
    public MockFreePublisher()
    {
        super();
    }

    @Override
    public void terminate()
    {
        System.out.println("LOL");
    }

    private PluginRespirator mockRespirator;

    @Override
    public PluginRespirator getPR()
    {
        return mockRespirator;
    }

    public void addPR(PluginRespirator resp)
    {
        mockRespirator = resp;
    }
}
