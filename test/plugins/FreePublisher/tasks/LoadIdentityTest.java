/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins.FreePublisher.tasks;

import freenet.client.FetchException;
import freenet.client.HighLevelSimpleClient;
import freenet.keys.FreenetURI;
import freenet.pluginmanager.PluginRespirator;
import java.io.File;
import java.io.IOException;
import plugins.FreePublisher.MockFreePublisher;
import plugins.FreePublisher.Identity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import plugins.FreePublisher.FreePublisher;
import static org.junit.Assert.*;

import static org.easymock.EasyMock.*;
/**
 *
 * @author zapu
 */
public class LoadIdentityTest {

    public LoadIdentityTest()
    {
    }

    private MockFreePublisher publisher;
    private PluginRespirator mockPR;
    private HighLevelSimpleClient mockHLSL;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp()
    {
        publisher = new MockFreePublisher();
        mockPR = createMock(PluginRespirator.class);
        publisher.addPR(mockPR);

        mockHLSL = createMock(HighLevelSimpleClient.class);

        expect(mockPR.getHLSimpleClient()).andReturn(mockHLSL);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void fileOpenFail() throws IOException, FetchException
    {
        expect(mockHLSL.fetch((FreenetURI) anyObject())).andThrow(new FetchException(1));
        LoadIdentityTask task = new LoadIdentityTask(new File("/bad/filename"));
        task.taskRun();
        System.out.println(task.getStatusString());
    }
}