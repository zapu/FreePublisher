package plugins.FreePublisher.ui;

import plugins.FreePublisher.FreePublisher;

import com.db4o.foundation.NotSupportedException;
import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageNode;
import freenet.clients.http.RedirectException;
import freenet.clients.http.Toadlet;
import freenet.clients.http.ToadletContext;
import freenet.clients.http.ToadletContextClosedException;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import java.io.IOException;
import java.net.URI;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author zapu
 */
public abstract class WebPage extends Toadlet
{
    public WebPage(PluginRespirator pr)
    {
        super(pr.getHLSimpleClient());
        
        this.pluginRespirator = pr;
        this.HLSC = pr.getHLSimpleClient();

        actionMap = new HashMap();
    }

    private Map<String, WebPageAction> actionMap;
    private PluginRespirator pluginRespirator;
    private HighLevelSimpleClient HLSC;

    public PluginRespirator getPR()
    {
        return pluginRespirator;
    }

    public HighLevelSimpleClient getHLSC()
    {
        return HLSC;
    }

    @Override
    public String path()
    {
        throw new NotSupportedException("WebPage's path() called,");
    }

    //For debugging purposes
    //Generic status consts:
    public static final int STATUS_NOERROR = 0; //When nothing has really happened yet (e.g. only info page was displayed)
    public static final int STATUS_ERROR = 100;
    public static final int STATUS_RUNNING = 201;
    public static final int STATUS_DISPATCHED = 202;
    public static final int STATUS_DONE = 203;

    private int debugStatusCode = 0;
    public int statusCode()
    {
        return debugStatusCode;
    }

    public void handleMethodGET(URI uri, final HTTPRequest request, final ToadletContext ctx)
	throws ToadletContextClosedException, IOException, RedirectException
    {
        ClassLoader origClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(FreePublisher.class.getClassLoader());
        try
        {
            PageNode node = ctx.getPageMaker().getPageNode("FreePublisher", ctx);
            HTMLNode htmlNode = node.outer;
            HTMLNode contentNode = node.content;

            callAction(request, contentNode, false);

            writeHTMLReply(ctx, 200, "OK", null, htmlNode.generate());
        }
        finally {
            Thread.currentThread().setContextClassLoader(origClassLoader);
        }
    }

    public void handleMethodPOST(URI uri, HTTPRequest request, final ToadletContext ctx)
	throws ToadletContextClosedException, IOException, RedirectException
    {
        ClassLoader origClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(FreePublisher.class.getClassLoader());
        try
        {
            PageNode node = ctx.getPageMaker().getPageNode("FreePublisher", ctx);
            HTMLNode htmlNode = node.outer;
            HTMLNode contentNode = node.content;

            callAction(request, contentNode, true);

            writeHTMLReply(ctx, 200, "OK", null, htmlNode.generate());
        }
        finally {
            Thread.currentThread().setContextClassLoader(origClassLoader);
        }
    }

    public void callAction(HTTPRequest request, HTMLNode node, boolean post)
    {
        String actionName = "";
        if(post && request.isPartSet("action"))
        {
            actionName = request.getPartAsStringFailsafe("action", 64);
        }
        else
        {
            actionName = request.getParam("action");
        }

        WebPageAction action = actionMap.get(actionName);
        if(action == null)
        {
            return;
        }

        debugStatusCode = action.handleAction(request, node, post);
    }

    protected void registerAction(String name, WebPageAction action)
    {
        actionMap.put(name, action);
    }

    protected void removeAction(String name)
    {
        actionMap.remove(name);
    }
}
