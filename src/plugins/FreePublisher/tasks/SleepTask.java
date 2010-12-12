package plugins.FreePublisher.tasks;

import freenet.support.HTMLNode;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zapu
 */
public class SleepTask extends FreenetTask
{
    private int iterations;
    private int time;
    private Status status;

    enum Status
    {
        NOT_YET_SLEEPING,
        STILL_SLEEPING,
        DONE_SLEEPING,
    }

    public SleepTask(int i, int time)
    {
        this.iterations = i;
        this.time = time;
        status = Status.NOT_YET_SLEEPING;
    }

    @Override
    public void taskStatus(HTMLNode contentNode)
    {
        switch(status)
        {
            case NOT_YET_SLEEPING:
                contentNode.addChild("p", "Not sleeping yet!");
                break;
            case STILL_SLEEPING:
                contentNode.addChild("p", "Will sleep for " + iterations + " * " + time + " ms more!");
                break;
            case DONE_SLEEPING:
                contentNode.addChild("p", "Done sleeping!");
                break;
        }
    }

    @Override
    public void taskRun()
    {
        status = Status.STILL_SLEEPING;
        while(iterations > 0)
        {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {

            }

            iterations--;
        }
        status = Status.DONE_SLEEPING;
    }
}
