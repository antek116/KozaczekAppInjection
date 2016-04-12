package example.kozaczekapp.timeZoneApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class JsonTimeZoneParser implements TImeZoneParser {

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
     * 
     * @param response
     * @return
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

}
