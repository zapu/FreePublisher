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
import plugins.FreePublisher.models.IdentityModel;
import plugins.FreePublisher.models.IdentityModel.CreateIdentityResult;
import plugins.FreePublisher.models.ModelCallback;


public class IdentityPage extends WebPage
{
    private IdentityModel identityModel;

    public IdentityPage(HighLevelSimpleClient hlsc, IdentityModel identityModel)
    {
        super(hlsc);

        this.identityModel = identityModel;

        registerAction("", new IndexAction());
        registerAction("loadIdentity", new LoadIdentityAction());
        registerAction("createIdentity", new CreateIdentityAction());
    }

    @Override
    public String path()
    {
        return "/publisher/identity";
    }

    class IndexAction implements WebPageAction
    {
        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            Identity iden = FreePublisher.getInstance().getIdentity();
            if(iden == null)
            {
                contentNode.addChild("p", "Identity not loaded.");
            }
            else
            {
                contentNode.addChild("p", "Loaded identity: " + iden.getName() + "(" + iden.getPublicKey() + ")");
            }
            
            return 0;
        }
    }

    class LoadIdentityAction implements WebPageAction, Runnable
    {
        private LoadIdentityAction backgroundTask = null;

        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            return 0;
        }

        public void run()
        {

        }
    }

    class CreateIdentityAction implements WebPageAction, Runnable, ModelCallback
    {
        private Thread thread = null;
        private boolean loadIdentity = false;
        private CreateIdentityResult result = null;
        private int taskStatus = STATUS_RUNNING;

        public int handleAction(HTTPRequest request, HTMLNode contentNode, boolean post)
        {
            try
            {
                if(thread != null)
                {
                    if(result == null)
                    {
                        contentNode.addChild("p", "Still running...");
                        return taskStatus;
                    }
                    else
                    {
                        contentNode.addChild("p", "Done!");
                        return STATUS_DONE;
                    }
                }
                else
                {
                    thread = new Thread(this);
                    thread.start();

                    contentNode.addChild("p", "Dispatched");

                    return STATUS_DISPATCHED;
                }
            }
            catch(Exception e)
            {
                contentNode.addChild("p", e.toString());
                System.err.println("CreateIdentityAction: " + e);
                return STATUS_ERROR;
            }
        }

        public void run()
        {
            try
            {
                result = identityModel.createIdentity();
                if(loadIdentity)
                {
                    FreePublisher.getInstance().setIdentity(result.identity, result.eventTable);
                }
            }
            catch(Exception ex)
            {
                System.err.println(ex.toString());
                taskStatus = STATUS_ERROR;
            }
        }

        public void statusCallback(int status)
        {
            System.err.println("CreateIdentityAction::statusCallback " + status);
        }
    }
}
