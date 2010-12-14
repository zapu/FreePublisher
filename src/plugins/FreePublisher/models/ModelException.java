package plugins.FreePublisher.models;

/**
 *
 * @author zapu
 */
public class ModelException extends Throwable
{
    private String message;
    private int status;

    public ModelException(String message, int status)
    {
        this.message = message;
        this.status = status;
    }

    @Override
    public String toString()
    {
        return message;
    }

    public int getStatus()
    {
        return status;
    }
}
