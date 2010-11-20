package plugins.FreePublisher.ui;

import plugins.FreePublisher.FreePublisher;

import com.db4o.foundation.NotSupportedException;
import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.PageNode;
import freenet.clients.http.RedirectException;
import freenet.clients.http.Toadlet;
import freenet.clients.http.ToadletContext;
import freenet.clients.http.ToadletContextClosedException;
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
public class WebPage extends Toadlet
{
    public WebPage(HighLevelSimpleClient hlsc)
    {
        super(hlsc);

        actionMap = new HashMap();
    }

    private Map<String, WebPageAction> actionMap;

    @Override
    public String path()
    {
        throw new NotSupportedException("WebPage's path() called,");
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

    private void callAction(HTTPRequest request, HTMLNode node, boolean post)
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

        action.handleAction(request, node, post);
    }

    public void registerAction(String name, WebPageAction action)
    {
        actionMap.put(name, action);
    }
}
