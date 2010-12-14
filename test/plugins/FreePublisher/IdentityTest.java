/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins.FreePublisher;

import freenet.client.InsertBlock;
import freenet.client.FetchException;
import freenet.client.HighLevelSimpleClient;
import freenet.client.InsertException;
import freenet.clients.http.ToadletContext;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import plugins.FreePublisher.MockFreePublisher;
import plugins.FreePublisher.Identity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import plugins.FreePublisher.FreePublisher;
import plugins.FreePublisher.models.IdentityModel;
import plugins.FreePublisher.ui.IdentityPage;
import static org.junit.Assert.*;

import static org.easymock.EasyMock.*;
/**
 *
 * @author zapu
 */
public class IdentityTest
{
    public IdentityTest()
    {
    }

    private PluginRespirator respirator;
    private HighLevelSimpleClient HLSL;
    private IdentityModel model;
    private IdentityPage page;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp()
    {
        new FreePublisher();

        respirator = createMock(PluginRespirator.class);
        HLSL = createMock(HighLevelSimpleClient.class);

        expect(respirator.getHLSimpleClient()).andReturn(HLSL).anyTimes();
        replay(respirator);

        model = new IdentityModel(respirator);
        
        page = new IdentityPage(HLSL, model);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() throws InsertException, FetchException, MalformedURLException
    {
        HTTPRequest request = createMock(HTTPRequest.class);
        expect(request.getParam("action")).andReturn("createIdentity").anyTimes();

        FreenetURI[] uris = new FreenetURI[2];
        uris[0] = new FreenetURI("SSK@aoozIUpFMIPR75lYFXZYOjS3Pb-6mt8wi0FYEs5bFaA,8-mK-~e~oYvJj5YUgXV9kxsURGq~BSPzb05aRkALqiI,AQACAAE/");
        uris[1] = new FreenetURI("SSK@aoozIUpFMIPR75lYFXZYOjS3Pb-6mt8wi0FYEs5bFaA,8-mK-~e~oYvJj5YUgXV9kxsURGq~BSPzb05aRkALqiI,AQACAAE/");

        expect(HLSL.generateKeyPair((String) anyObject())).andReturn(uris).once();
        expect(HLSL.insert((InsertBlock) anyObject(), eq(false), (String) anyObject())).andReturn(uris[1]);
        
        replay(request);
        replay(HLSL);
        
        HTMLNode node = new HTMLNode("div");

        page.callAction(request, node, false);
        assertEquals(IdentityPage.STATUS_DISPATCHED, page.statusCode());
        page.callAction(request, node, false);
        while(page.statusCode() == IdentityPage.STATUS_RUNNING)
        {
            //simulate refreshing the page till user sees "done"
            page.callAction(request, node, false);
        }

        assertEquals(IdentityPage.STATUS_DONE, page.statusCode());
    }
}
