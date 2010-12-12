package plugins.FreePublisher.tasks;

import freenet.client.HighLevelSimpleClient;
import freenet.keys.FreenetURI;
import freenet.support.HTMLNode;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.Identity;

/**
 *
 * @author zapu
 */
public class CreateIdentityTask extends FreenetTask
{
    private boolean loadAfter;

    public CreateIdentityTask(boolean loadAfter)
    {
        this.loadAfter = loadAfter;
    }

    @Override
    public void taskStatus(HTMLNode contentNode)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void taskRun()
    {
        HighLevelSimpleClient HLSL = FreePublisher.getInstance().getPR().getHLSimpleClient();

        try
        {
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
        }
        catch(Exception e)
        {

        }
    }

}
