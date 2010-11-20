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

/**
 *
 * @author zapu
 */
public class MainToadlet extends Toadlet
{
    public MainToadlet(HighLevelSimpleClient hlsc)
    {
        super(hlsc);
    }

    @Override
    public String path()
    {
        return "/publisher/";
    }

    public void handleMethodGET(URI uri, final HTTPRequest request, final ToadletContext ctx)
	throws ToadletContextClosedException, IOException, RedirectException
    {
            ClassLoader origClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(FreePublisher.class.getClassLoader());
            try
            {
                System.err.println("handleMethodGET");
                PageNode node = ctx.getPageMaker().getPageNode("FreePublisher", ctx);
                HTMLNode htmlNode = node.outer;
                HTMLNode contentNode = node.content;

                contentNode.addChild("div", "class", "configprefix", "Freepublisher Status");

                PluginRespirator pr = FreePublisher.getInstance().getPR();

                HTMLNode form = pr.addFormChild(contentNode, "", "form name");
                form.addChild("span","class","config").addChild("input",
				new String[] { "class", "type", "name" },
				new String[] { "config", "text", "url" });
                form.addChild("input",
				new String[] { "type", "value", "style" },
				new String[] { "submit", "Add Feed", "margin-top:10px; margin-bottom:20px;" });

                /*MainPage page = new MainPage(freereader);
                PageNode p = ctx.getPageMaker().getPageNode(Freereader.pluginName, ctx);
                HTMLNode pageNode = p.outer;
                HTMLNode contentNode = p.content;
                page.writeContent(request, contentNode);
                writeHTMLReply(ctx, 200, "OK", null, pageNode.generate());*/
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
            System.err.println("handleMethodPOST");

            String reqUri = request.getPartAsStringFailsafe("url", 128);
            HighLevelSimpleClient client = FreePublisher.getInstance().getPR().getHLSimpleClient();
            try
            {
                FetchResult result = client.fetch(new FreenetURI(reqUri));
                writeHTMLReply(ctx, 200, "OK", null, result.asByteArray().toString());
            }
            catch (FetchException ex)
            {
                System.err.println(ex);
                writeHTMLReply(ctx, 200, "OK", null, ex.toString());
            }

        }
        finally {
            Thread.currentThread().setContextClassLoader(origClassLoader);
        }
    }
}
