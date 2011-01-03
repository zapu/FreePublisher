package plugins.FreePublisher.ui;

import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageMaker;
import freenet.clients.http.ToadletContainer;
import freenet.pluginmanager.PluginRespirator;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.models.EventTableModel;
import plugins.FreePublisher.models.IdentityModel;

/**
 *
 * @author zapu
 */
public class UserInterface
{
    private final ToadletContainer toadletContainer;
    private final PluginRespirator pr;
    private final HighLevelSimpleClient client;
    private final PageMaker pageMaker;

    private MainToadlet mainToadlet;
    private IdentityPage identityPage;
    private EventTablePage eventTablePage;

    public UserInterface(PluginRespirator pr)
    {
        this.pr = pr;
        this.toadletContainer = pr.getToadletContainer();
        this.client = pr.getHLSimpleClient();
        this.pageMaker = pr.getPageMaker();
    }

    public void load()
    {
        FreePublisher publisher = FreePublisher.getInstance();

        pageMaker.addNavigationCategory("/publisher/", "FreePublisher", "FreePublisher", FreePublisher.getInstance());

        toadletContainer.register(mainToadlet = new MainToadlet(client),
                "FreePublisher", "/publisher/", true, "Status", "Status", true, null);

        identityPage = new IdentityPage(pr, (IdentityModel) publisher.getModel(IdentityModel.class));
        
        toadletContainer.register(identityPage, "FreePublisher", "/publisher/identity", true, "Identity", "Identity", true, null);

        eventTablePage = new EventTablePage(pr, (EventTableModel) publisher.getModel(EventTableModel.class));

        toadletContainer.register(eventTablePage, "FreePublisher", eventTablePage.path(), true, "Browse", "Browse", true, null);
        
        //toadletContainer.register(FreePublisher.getInstance().taskManager, ""
        //        + "FreePublisher", "/publisher/tasks", true, "Tasks", "Tasks", true, null);
    }

    public void unload()
    {
        toadletContainer.unregister(mainToadlet);
        toadletContainer.unregister(identityPage);
        toadletContainer.unregister(eventTablePage);
        toadletContainer.unregister(FreePublisher.getInstance().taskManager);

        pageMaker.removeNavigationCategory("FreePublisher");
    }
}
