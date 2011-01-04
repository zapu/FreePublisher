package plugins.FreePublisher.ui;

import freenet.support.api.HTTPUploadedFile;
import java.io.IOException;

import freenet.client.HighLevelSimpleClient;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.Publisher;
import plugins.FreePublisher.models.IdentityModel;
import plugins.FreePublisher.models.IdentityModel.IdentityResult;
import plugins.FreePublisher.models.ModelCallback;


public class IdentityPage extends WebPage
{
    private IdentityModel identityModel;

    public IdentityPage(Publisher publisher)
    {
        super(publisher);

        identityModel = publisher.getModel(IdentityModel.class);

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
            Identity identity = getPublisher().identity;
            if(identity == null)
            {
                contentNode.addChild("p", "Identity not loaded.");
            }
            else
            {
                contentNode.addChild("p", "Loaded identity: " + identity.getName() + "(" + identity.getPublicKey() + ")");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try
                {
                    identity.save(stream);
                    contentNode.addChild("pre", stream.toString());
                }
                catch(Exception e)
                {
                    System.err.println(e);
                }
            }
            
            return 0;
        }
    }

    class LoadIdentityAction implements WebPageAction, Runnable
    {
        private Thread thread = null;
        private IdentityResult result = null;
        private int taskStatus = STATUS_RUNNING;
        private InputStream identityStream = null;

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
                    if(post)
                    {
                        HTTPUploadedFile uploadedFile = request.getUploadedFile("file");
                        if(uploadedFile == null)
                            throw new Exception("Form error");

                        identityStream = uploadedFile.getData().getInputStream();

                        thread = new Thread(this);
                        thread.start();

                        contentNode.addChild("p", "Dispatched");

                        return STATUS_DISPATCHED;
                    }
                    else
                    {
                        HTMLNode form = getPR().addFormChild(contentNode, "", "form name");
                        form.addChild("input",
                                        new String[] { "class", "type", "name" },
                                        new String[] { "config", "file", "file" });
                        form.addChild("input",
                                        new String[] { "type", "name", "value" },
                                        new String[] { "hidden", "action", "loadIdentity" });
                        form.addChild("input",
                                        new String[] { "type", "value", "style" },
                                        new String[] { "submit", "Add Feed", "margin-top:10px; margin-bottom:20px;" });

                        return STATUS_NOERROR;
                    }
                }
            }
            catch(Exception e)
            {
                contentNode.addChild("p", e.toString());
                System.err.println("LoadIdentityAction: " + e);
                return STATUS_ERROR;
            }
        }

        public void run()
        {
            try
            {
                result = identityModel.loadIdentity(identityStream);
                getPublisher().identity = result.identity;
                getPublisher().eventTable = result.eventTable;
            }
            catch(Exception ex)
            {
                System.err.println(ex.toString());
                taskStatus = STATUS_ERROR;
            }
            finally
            {
                try
                {
                    identityStream.close();
                }
                catch(IOException e)
                {

                }

                this.thread = null;
            }
        }
    }

    class CreateIdentityAction implements WebPageAction, Runnable, ModelCallback
    {
        private Thread thread = null;
        private boolean loadIdentity = true;
        private IdentityResult result = null;
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
                    getPublisher().identity = result.identity;
                    getPublisher().eventTable = result.eventTable;
                }
            }
            catch(Exception ex)
            {
                System.err.println(ex.toString());
                taskStatus = STATUS_ERROR;
            }
            finally
            {
                this.thread = null;
            }
        }

        public void statusCallback(int status)
        {
            System.err.println("CreateIdentityAction::statusCallback " + status);
        }
    }
}
