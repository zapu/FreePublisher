package plugins.FreePublisher.ui;

import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;

/**
 *
 * @author zapu
 */
public interface WebPageAction
{
    public void handleAction(HTTPRequest request, HTMLNode contentNode, boolean post);
}
