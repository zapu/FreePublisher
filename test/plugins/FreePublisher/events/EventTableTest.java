/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins.FreePublisher.events;

import java.io.IOException;
import org.jdom.JDOMException;
import plugins.FreePublisher.events.EventTable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zapu
 */
public class EventTableTest {

    public EventTableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void creatingEventFromEnum() throws Exception
    {
        EventType et = EventType.ADD;
        Event event = et.createEvent();
        assertTrue(event instanceof EventAdd);
    }

    @Test
    public void loadingEventTable() throws JDOMException, IOException, Exception
    {
        String testXML = "<eventTable>"
                + "<events>"
                + "<event type=\"0\">"
                + "<title>test Title</title>"
                + "<getURI>test URI</getURI>"
                + "</event>"
                + "</events>"
                + "</eventTable>";
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testXML.getBytes());
        EventTable table = new EventTable();
        table.loadEventTable(inputStream);

        assertEquals(1, table.getEvents().size());
    }

}