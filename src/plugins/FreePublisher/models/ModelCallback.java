package plugins.FreePublisher.models;

/**
 * Might be used later, for model methods that take long and
 * want to raport back to WebPages about their workings
 *
 * @author zapu
 */
public interface ModelCallback
{
    public void statusCallback(int status);
}
