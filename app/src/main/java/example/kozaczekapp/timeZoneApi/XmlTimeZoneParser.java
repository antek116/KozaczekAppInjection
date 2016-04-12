package example.kozaczekapp.timeZoneApi;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import example.kozaczekapp.kozaczekItems.Article;

public class XmlTimeZoneParser implements ITimeZoneParser {
    private static final String XML_TYPE = "xml";
    private static final String ITEM_TAG_NAME ="timestamp";
    private static final String ENCODING = "UTF-8";
    private static final int FIRST_ELEMENT = 0;
    private static final int TIME_STAMP_ELEMENT = 0;

    @Override
    public String parseResponse(String response) {
        if (response == null) {
            return null;
        }
        NodeList nodeList = null;
        Document doc;
        try {
            doc = buildDocumentFromInputStream(response);
            nodeList = doc.getElementsByTagName(ITEM_TAG_NAME);
            int x =2;
        } catch ( ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        if (nodeList != null)
        {
            return getTimeStamp(nodeList);
        }
        return null;
    }

    private String getTimeStamp(NodeList nodeList) {
        Node node = nodeList.item(FIRST_ELEMENT);
        Node nodeChild = node.getChildNodes().item(TIME_STAMP_ELEMENT);
        return nodeChild.getNodeValue();
    }

    private Document buildDocumentFromInputStream(String response) throws IOException,
            ParserConfigurationException,
            SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        InputStream inStream = IOUtils.toInputStream(response, ENCODING);
        return db.parse(inStream);
    }

    @Override
    public String getType() {
        return XML_TYPE;
    }
}
