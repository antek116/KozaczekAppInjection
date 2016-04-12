package example.kozaczekapp.timeZoneApi;

import org.json.JSONException;

public interface ITimeZoneParser {

    String parseResponse(String response) throws JSONException;

    String getType();
}
