package example.kozaczekapp.timeZoneApi;

public class JsonTimeZoneParser implements ITimeZoneParser {

    private static final String TYPE_JSON ="json";

    @Override
    public String parseResponse(String response) {
        return null;
    }

    @Override
    public String getType() {
        return TYPE_JSON;
    }
}
