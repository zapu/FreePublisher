package plugins.FreePublisher.ui;

import plugins.FreePublisher.ui.IdentityPage;
import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageMaker;
import freenet.clients.http.ToadletContainer;
import freenet.pluginmanager.PluginRespirator;
import plugins.FreePublisher.FreePublisher;

/**
 *
 * @author zapu
 */
public class UserInterface
{
    private final ToadletContainer toadletContainer;
    private final HighLevelSimpleClient client;
    private final PageMaker pageMaker;

    private MainToadlet mainToadlet;
    private IdentityPage identityToadlet;

    public UserInterface(PluginRespirator pr)
    {
        toadletContainer = pr.getToadletContainer();
        client = pr.getHLSimpleClient();
        pageMaker = pr.getPageMaker();
    }

    public void load()
    {
        pageMaker.addNavigationCategory("/publisher/", "FreePublisher", "FreePublisher", FreePublisher.getInstance());

        toadletContainer.register(mainToadlet = new MainToadlet(client), "FreePublisher", "/publisher/", true, "Status", "Status", true, null);
        toadletContainer.register(identityToadlet = new IdentityPage(client), "FreePublisher", "/publisher/identity", true, "Identity", "Identity", true, null);
    }

    public void unload()
    {
        toadletContainer.unregister(mainToadlet);
        toadletContainer.unregister(identityToadlet);

        pageMaker.removeNavigationCategory("FreePublisher");
    }
}