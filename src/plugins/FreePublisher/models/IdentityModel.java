package plugins.FreePublisher.models;

import freenet.client.ClientMetadata;
import freenet.client.InsertBlock;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.io.ArrayBucket;
import freenet.support.io.Closer;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import plugins.FreePublisher.Identity;
import plugins.FreePublisher.events.EventTable;

/**
 *
 * @author zapu
 */
public class IdentityModel extends FreenetModel
{
    public IdentityModel(PluginRespirator respirator)
    {
        super(respirator);
    }

    public class CreateIdentityResult
    {
        public Identity identity;
        public EventTable eventTable;
    }

    public CreateIdentityResult createIdentity() throws Exception
    {
        FreenetURI[] keyPair = getHLSL().generateKeyPair("");
        String insertKey = keyPair[0].toString();
        String requestKey = keyPair[1].toString();

        String privateKey = insertKey.substring(4, insertKey.length() - 1);
        String publicKey = requestKey.substring(4, requestKey.length() - 1);

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        SecretKey secretKey = keyGenerator.generateKey();
        //loading secret key = SecretKey secretKey = new SecretKeySpec(bytes, "AES");

        Identity identity = new Identity(privateKey, publicKey, secretKey.getEncoded(), "identity");

        EventTable table = new EventTable();

        ArrayBucket arrayBucket = new ArrayBucket("eventTableBucket");
        OutputStream eventStream = arrayBucket.getOutputStream();
        table.outputEventTable(eventStream);
        eventStream.flush();
        Closer.close(eventStream);

        getHLSL().insert(new InsertBlock(arrayBucket, new ClientMetadata("text/plain"), new FreenetURI("USK@" + privateKey + "/events.xml/0")), false, null);

        CreateIdentityResult result = new CreateIdentityResult();
        result.identity = identity;
        result.eventTable = table;
        return result;
    }

    public Identity loadIdentity(InputStream stream)
    {

        System.err.println("CreateIdentity() called");

        return null;
    }
}
