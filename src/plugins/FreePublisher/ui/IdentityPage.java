package plugins.FreePublisher.ui;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import java.io.IOException;
import java.net.URI;

import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageNode;
import freenet.clients.http.RedirectException;
import freenet.clients.http.Toadlet;
import freenet.clients.http.ToadletContext;
import freenet.clients.http.ToadletContextClosedException;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Identity;


public class IdentityPage extends WebPage
{
    class IndexAction implements WebPageAction
    {
        public void handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            contentNode.addChild("p", "lol action");
        }
    }

    class LoadIdentityAction implements WebPageAction
    {
        public void handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            if(!post)
            {

            }
            else
            {
                contentNode.addChild("p", "lol action");
            }
        }
    }

    class CreateIdentityAction implements WebPageAction
    {
        public void handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            
        }
    }

    public IdentityPage(HighLevelSimpleClient hlsc)
    {
        super(hlsc);

        registerAction("", new IndexAction());
        registerAction("loadIdentity", new LoadIdentityAction());
        registerAction("createIdentity", new CreateIdentityAction());
    }

    @Override
    public String path()
    {
        return "/publisher/identity";
    }
}
