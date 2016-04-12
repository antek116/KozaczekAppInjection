package example.kozaczekapp.timeZoneApi;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages parser things
 */
public class JsonTimeZoneParser implements ITimeZoneParser {

    private static final String TYPE_JSON ="json";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String STATUS_KEY = "status";
    private static String status;
    private String timestamp;

    /**
     * Gets status of response
     * @param response - string formatted as Json Object
     * @return response status: OK - correct, FAIL - wrong
     */
    public static String getResponseStatus(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            status = String.valueOf(reader.get(STATUS_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Parses Json response
     * @param response to be parsed
     * @return timestamp from parsed response
     */
    @Override
    public String parseResponse(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            timestamp = String.valueOf(reader.get(TIMESTAMP_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    @Override
    public String getType() {
        return TYPE_JSON;
    }




}
