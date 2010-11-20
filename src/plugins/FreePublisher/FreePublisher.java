package plugins.FreePublisher;

import plugins.FreePublisher.ui.UserInterface;
import freenet.l10n.BaseL10n.LANGUAGE;
import freenet.node.Version;
import freenet.pluginmanager.*;
import freenet.support.Logger;

/**
 *
 * @author zapu
 */
public class FreePublisher implements FredPlugin, FredPluginThreadless, FredPluginL10n
{
    private static FreePublisher instance;
    public static synchronized FreePublisher getInstance()
    {
        return instance;
    }

    private UserInterface userInterface;
    private PluginRespirator respirator;

    public Identity identity;

    public FreePublisher()
    {
        instance = this;
        identity = null;
    }

    public void runPlugin(PluginRespirator pr)
    {
        System.err.println("FreePublisher started.");
        
        respirator = pr;

        userInterface = new UserInterface(pr);
        userInterface.load();
    }

    public void terminate()
    {
        System.err.println("FreePublisher terminated.");

        userInterface.unload();
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

}
