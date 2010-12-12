package plugins.FreePublisher.tasks;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import freenet.client.ClientMetadata;
import freenet.client.HighLevelSimpleClient;
import freenet.client.InsertBlock;
import freenet.keys.FreenetURI;
import freenet.support.HTMLNode;
import freenet.support.io.ArrayBucket;
import freenet.support.io.Closer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.events.EventTable;

/**
 *
 * @author zapu
 */
public class CreateIdentityTask extends FreenetTask
{
    private boolean loadAfter;
    private File identityFile;
    private String error;

    public CreateIdentityTask(boolean loadAfter, File file)
    {
        this.loadAfter = loadAfter;
        this.identityFile = file;
        this.createStatus = Status.Waiting;
    }

    enum Status
    {
        Waiting,
        GeneratingKeys,
        InsertingEventTable,
        Done,
        Error,
    }

    private Status createStatus;

    @Override
    public void taskStatus(HTMLNode contentNode)
    {
        contentNode.addChild("h1", createStatus.toString());
    }

    @Override
    public void taskRun()
    {
        HighLevelSimpleClient HLSL = FreePublisher.getInstance().getPR().getHLSimpleClient();

        try
        {
            createStatus = Status.GeneratingKeys;

            FreenetURI[] keyPair = HLSL.generateKeyPair("");
            String insertKey = keyPair[0].toString();
            String requestKey = keyPair[1].toString();

            String privateKey = insertKey.substring(4, insertKey.length() - 1);
            String publicKey = requestKey.substring(4, requestKey.length() - 1);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            SecretKey secretKey = keyGenerator.generateKey();
            //loading secret key = SecretKey secretKey = new SecretKeySpec(bytes, "AES");

            Identity identity = new Identity(privateKey, publicKey, secretKey.getEncoded(), "identity");
            identity.save(new FileOutputStream(identityFile));

            createStatus = Status.InsertingEventTable;

            EventTable table = new EventTable();
            
            ArrayBucket arrb = new ArrayBucket("eventTableBucket");
            OutputStream eventStream = arrb.getOutputStream();
            table.outputEventTable(eventStream);
            eventStream.flush();
            Closer.close(eventStream);

            HLSL.insert(new InsertBlock(arrb, new ClientMetadata("text/plain"), new FreenetURI("USK@" + privateKey + "/events.xml/0")), false, null);

            if(loadAfter)
            {
                FreePublisher.getInstance().identity = identity;
                FreePublisher.getInstance().eventTable = table;
            }

            createStatus = Status.Done;
        }
        catch(Exception e)
        {
            error = e.toString();
            createStatus = Status.Error;
            notifyError();
        }
    }

}
