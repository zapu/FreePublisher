package plugins.FreePublisher.models;

import com.db4o.foundation.NotSupportedException;
import freenet.client.ClientMetadata;
import freenet.client.InsertBlock;
import freenet.client.InsertException;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.api.Bucket;
import java.net.MalformedURLException;

/**
 *
 * @author zapu
 */
public class DataModel extends FreenetModel
{
    public DataModel(PluginRespirator respirator)
    {
        super(respirator);
    }

    public FreenetURI insertBucket(Bucket bucket, ClientMetadata meta, FreenetURI uri) throws InsertException, MalformedURLException
    {
        System.err.println("InsertBucket started: " + bucket.size() + " bytes of " + meta);

        if(uri == null)
        {
            uri = new FreenetURI(getHLSL().generateKeyPair("")[0] + "file.bin");
        }

        uri = getHLSL().insert(new InsertBlock(bucket, meta, uri), false, null);

        System.err.println("InsertBucket done");

        return uri;
    }
}
