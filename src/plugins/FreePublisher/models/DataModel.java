package plugins.FreePublisher.models;

import com.db4o.foundation.NotSupportedException;
import freenet.client.ClientMetadata;
import freenet.client.InsertBlock;
import freenet.client.InsertException;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.api.Bucket;

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

    public FreenetURI insertBucket(Bucket bucket, ClientMetadata meta, FreenetURI uri) throws InsertException
    {
        if(uri == null)
        {
            uri = getHLSL().generateKeyPair("")[0];
        }

        return getHLSL().insert(new InsertBlock(bucket, meta, uri), false, null);
    }
}
