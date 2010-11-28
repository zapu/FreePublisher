package plugins.FreePublisher;

import java.io.File;
import java.util.List;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author zapu
 */
public class NewClass
{
    public static void main(String[] argv)
    {
        parseTest();
    }

    public static void parseTest()
    {
        try
        {
            /*SAXBuilder builder = new SAXBuilder();
            //builder.setValidation(true);
            builder.setIgnoringElementContentWhitespace(true);
            Document doc = builder.build(new File("C:/dev/freenet/FreePublisher/build.xml"));

            Element root = doc.getRootElement();
            System.err.println("root: " + root.getName() + " " + root.getAttribute("default"));
            List<Element> allChildren = root.getChildren();
            for(Element e : allChildren)
            {
                System.err.println(e.getName());
            }*/

            Identity id = new Identity("private", "public", new byte[]{ 0x00, 0x00 }, "heh" );
            id.saveToFile(null);

        }
        catch(Exception e) //gotta catch 'em all
        {
            System.err.println(e);
        }
    }
}
