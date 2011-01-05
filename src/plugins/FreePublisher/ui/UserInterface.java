package plugins.FreePublisher.ui;

import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageMaker;
import freenet.clients.http.ToadletContainer;
import freenet.pluginmanager.PluginRespirator;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Publisher;
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

    private FreePublisher plugin;
    private Publisher publisher;

    private IdentityPage identityPage;
    private EventTablePage eventTablePage;
    private InsertPage insertPage;

    public UserInterface(FreePublisher plugin)
    {
        this.plugin = plugin;
        this.pr = plugin.getPR();
        this.publisher = plugin.getPublisher();

        this.toadletContainer = pr.getToadletContainer();
        this.client = pr.getHLSimpleClient();
        this.pageMaker = pr.getPageMaker();
    }

    public void load()
    {
        pageMaker.addNavigationCategory("/publisher/", "FreePublisher", "FreePublisher", plugin);
        
        identityPage = new IdentityPage(publisher);
        toadletContainer.register(identityPage, "FreePublisher", identityPage.path(), true, "Identity", "Identity", true, null);

        eventTablePage = new EventTablePage(publisher);
        toadletContainer.register(eventTablePage, "FreePublisher", eventTablePage.path(), true, "Browse", "Browse", true, null);

        insertPage = new InsertPage(publisher);
        toadletContainer.register(insertPage, "FreePublisher", insertPage.path(), true, "Insert", "Insert", true, null);
    }

    public void unload()
    {
        toadletContainer.unregister(identityPage);
        toadletContainer.unregister(eventTablePage);

        pageMaker.removeNavigationCategory("FreePublisher");
    }
}
