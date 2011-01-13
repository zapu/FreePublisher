package plugins.FreePublisher.models;

import freenet.client.ClientMetadata;
import freenet.client.FetchResult;
import freenet.client.InsertBlock;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.io.ArrayBucket;
import freenet.support.io.Closer;
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

    public class IdentityResult
    {
        public Identity identity;
        public EventTable eventTable;
    }

    public IdentityResult createIdentity() throws Exception
    {
        System.err.println("CreateIdentity started");

        FreenetURI[] keyPair = getHLSL().generateKeyPair("");
        String insertKey = keyPair[0].toString();
        String requestKey = keyPair[1].toString();

        String privateKey = insertKey.substring(4, insertKey.length() - 1);
        String publicKey = requestKey.substring(4, requestKey.length() - 1);

        System.err.println("Generated keypair: " + privateKey.substring(0, 4) + " " + publicKey.substring(0, 4));

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        SecretKey secretKey = keyGenerator.generateKey();
        //loading secret key = SecretKey secretKey = new SecretKeySpec(bytes, "AES");

        System.err.println("Generated secret key: " + Identity.byteToHexString(secretKey.getEncoded()).substring(0,8));

        Identity identity = new Identity(privateKey, publicKey, secretKey.getEncoded(), "identity");

        EventTable table = new EventTable();

        ArrayBucket arrayBucket = new ArrayBucket("eventTableBucket");
        OutputStream eventStream = arrayBucket.getOutputStream();
        table.outputEventTable(eventStream);
        eventStream.flush();
        Closer.close(eventStream);

        System.err.println("Inserting " + arrayBucket.size() + " bytes");

        getHLSL().insert(new InsertBlock(arrayBucket, new ClientMetadata("text/plain"), new FreenetURI("USK@" + privateKey + "/events.xml/0")), false, null);

        arrayBucket.free();

        System.err.println("Inserted EventTable. Success!");

        IdentityResult result = new IdentityResult();
        result.identity = identity;
        result.eventTable = table;
        return result;
    }

    public IdentityResult loadIdentity(InputStream stream) throws Exception
    {
        System.err.println("LoadIdentity started.");

        Identity identity = new Identity(stream);

        System.err.println("Identity file parsed.");

        FetchResult fetch = getHLSL().fetch(new FreenetURI("USK@" + identity.getPublicKey() + "/events.xml/0"));

        System.err.println("Fetched event table.");

        EventTable table = new EventTable();
        table.loadEventTable(fetch.asBucket().getInputStream());

        System.err.println("Event table parsed.");
        
        IdentityResult result = new IdentityResult();
        result.identity = identity;
        result.eventTable = table;
        return result;
    }
}
