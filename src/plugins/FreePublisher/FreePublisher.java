package plugins.FreePublisher;

import plugins.FreePublisher.ui.UserInterface;
import freenet.l10n.BaseL10n.LANGUAGE;
import freenet.pluginmanager.*;

/**
 *
 * @author zapu
 */
public class FreePublisher implements FredPlugin, FredPluginThreadless, FredPluginL10n
{
    private Publisher publisher;
    private UserInterface userInterface;
    private PluginRespirator respirator;

    public FreePublisher()
    {

    }

    public void runPlugin(PluginRespirator pr)
    {
        System.err.println("FreePublisher started.");
        
        respirator = pr;

        publisher = new Publisher(respirator);
        publisher.init();

        userInterface = new UserInterface(this);
        userInterface.load();
    }

    public void terminate()
    {
        System.err.println("FreePublisher terminated.");

        userInterface.unload();
        publisher.deinit();
    }

    public String getVersion()
    {
        return "0.1alpha";
    }

    public long getRealVersion()
    {
        return 1;
    }

    public void setLanguage(LANGUAGE lng)
    {
        //not supported yet, ignore silently
    }

    public String getString(String string)
    {
        //not supported, just return key
        return string;
    }

    public PluginRespirator getPR()
    {
        return respirator;
    }

    public Publisher getPublisher()
    {
        return publisher;
    }
}
