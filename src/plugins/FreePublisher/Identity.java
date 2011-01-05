package plugins.FreePublisher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author zapu
 */
public class Identity
{
    public Identity(InputStream stream) throws JDOMException, IOException, Exception
    {
        SAXBuilder builder = new SAXBuilder();
        //builder.setValidation(true);
        builder.setIgnoringElementContentWhitespace(true);
        Document doc = builder.build(stream);

        Element root = doc.getRootElement();

        Attribute nameAttr = root.getAttribute("name");
        if(nameAttr == null)
            throw new Exception("No name attr.");

        name = nameAttr.getValue();

        Element keys = root.getChild("keys");
        if(keys == null)
            throw new Exception("No keys element.");

        Attribute keysTypeAttr = keys.getAttribute("type");
        if(keysTypeAttr == null)
            throw new Exception("No keys->type attribute.");

        int keysType = Integer.parseInt(keysTypeAttr.getValue());
        if(keysType == 0)
        {
            loadKeys(keys);
        }
        else if(keysType == 1)
        {
            /*String decrypted = "";

            Document doc2 = builder.build(new StringReader(decrypted));
            Element keysRoot = doc2.getRootElement();
            loadKeys(keysRoot);*/
            throw new Exception("key encryption not supported (yet).");
        }
    }

    public Identity(String privateKey, String publicKey, byte[] encryptionKey, String name)
    {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.encryptionKey = encryptionKey;
        this.name = name;
    }

    private void loadKeys(Element keysElement)
    {
        privateKey = keysElement.getChild("private").getTextTrim();
        publicKey = keysElement.getChild("public").getTextTrim();
        encryptionKey = hexStringToByteArray(keysElement.getChild("encryption").getText());
    }

    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteToHexString(byte[] byteArray)
    {
        StringBuilder result = new StringBuilder();
        for (byte b:byteArray)
        {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public void save(OutputStream stream) throws IOException
    {
        Document doc = new Document();
        Element root = new Element("identity");
        root.setAttribute(new Attribute("name", name));

        Element keys = new Element("keys");
        keys.setAttribute("type", "0");
        keys.getChildren().add(new Element("private").setText(privateKey));
        keys.getChildren().add(new Element("public").setText(publicKey));
        keys.getChildren().add(new Element("encryption").setText(byteToHexString(encryptionKey)));

        root.getChildren().add(keys);

        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, stream);
    }

    private String name;

    private String privateKey;
    private String publicKey;
    private byte[] encryptionKey;

    public String getName() { return name; }
    public String getPublicKey() { return publicKey; }
    public String getPrivateKey() { return privateKey; }
    public byte[] getEncryptionKey() { return encryptionKey; }
}
