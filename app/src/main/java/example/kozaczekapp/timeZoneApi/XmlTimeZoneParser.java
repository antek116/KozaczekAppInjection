package example.kozaczekapp.timeZoneApi;

public class XmlTimeZoneParser implements ITimeZoneParser {
    private static final String XML_TYPE = "xml";

    @Override
    public String parseResponse(String response) {
        return null;
    }

    @Override
    public String getType() {
        return XML_TYPE;
    }
}
