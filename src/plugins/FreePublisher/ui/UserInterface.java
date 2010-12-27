package plugins.FreePublisher.ui;

import plugins.FreePublisher.ui.IdentityPage;
import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageMaker;
import freenet.clients.http.ToadletContainer;
import freenet.pluginmanager.PluginRespirator;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.models.IdentityModel;

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

    private IdentityModel identityModel;

    public UserInterface(PluginRespirator pr)
    {
        toadletContainer = pr.getToadletContainer();
        client = pr.getHLSimpleClient();
        pageMaker = pr.getPageMaker();

        identityModel = new IdentityModel(pr);
    }

    public void load()
    {
        pageMaker.addNavigationCategory("/publisher/", "FreePublisher", "FreePublisher", FreePublisher.getInstance());

        toadletContainer.register(mainToadlet = new MainToadlet(client),
                "FreePublisher", "/publisher/", true, "Status", "Status", true, null);
        
        toadletContainer.register(identityToadlet = new IdentityPage(client, identityModel),
                "FreePublisher", "/publisher/identity", true, "Identity", "Identity", true, null);
        
        toadletContainer.register(FreePublisher.getInstance().taskManager, ""
                + "FreePublisher", "/publisher/tasks", true, "Tasks", "Tasks", true, null);
    }

    public void unload()
    {
        toadletContainer.unregister(mainToadlet);
        toadletContainer.unregister(identityToadlet);
        toadletContainer.unregister(FreePublisher.getInstance().taskManager);

        pageMaker.removeNavigationCategory("FreePublisher");
    }
}
