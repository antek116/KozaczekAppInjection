package example.kozaczekapp.timeZoneApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To use Unit tests on JSON you have to download "real" JSON package library
 */
public class JsonTimeZoneParserTest {

    private static final String STATUS_FAIL = "FAIL";
    private static final String STATUS_OK = "OK";
    private static final String WRONG_RESPONSE_FORMAT = "{ sdasd }";
    private static final String CORRECT_RESPONSE_WITH_CORRECT_API_KEY = "{\"status\":\"OK\"," +
            "\"message\":\"\",\"" + "countryCode\":\"CA\",\"zoneName\":\"America\\/Toronto\"," +
            "\"abbreviation\":\"EDT\"," + "\"gmtOffset\":\"-14400\",\"dst\":\"1\",\"timestamp\"" +
            ":1460445406}";
    private static final String CORRECT_RESPONSE_WITH_WRONG_API_KEY = "{\"status\":\"FAIL\"," +
            "\"message\":\"Invalid API key.\",\"countryCode\":\"\",\"zoneName\":\"\"," +
            "\"abbreviation\":\"\",\"gmtOffset\":0,\"dst\":0,\"timestamp\":0}";
    private static final int DEFAULT_TIMESTAMP = 0;
    private String response;
    private JsonTimeZoneParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new JsonTimeZoneParser();
    }

    @After
    public void tearDown() throws Exception {
        response = null;
        parser = null;
    }

    //TODO: when then... + przeniesc lokalnie zmienne niektore
    @Test
    public void shouldReturnNotNullResponse() throws Exception {
        response = CORRECT_RESPONSE_WITH_CORRECT_API_KEY;
        String jsonParsed = parser.parseResponse(response);
        assertNotNull(jsonParsed);
    }

    @Test
    public void shouldReturnFAILStatusWhenWrongAPI() throws Exception {
        response = CORRECT_RESPONSE_WITH_WRONG_API_KEY;
        String actualStatus = JsonTimeZoneParser.getResponseStatus(response);
        assertEquals(STATUS_FAIL, actualStatus);
    }

    @Test
    public void shouldReturnOKStatusWhenCorrectAPI() throws Exception {
        response = CORRECT_RESPONSE_WITH_CORRECT_API_KEY;
        String actualStatus = JsonTimeZoneParser.getResponseStatus(response);
        assertEquals(STATUS_OK, actualStatus);
    }

    @Test
    public void shouldReturnNumberGreaterThanDefaultTimestampWhenOKStatus() throws Exception {
        response = CORRECT_RESPONSE_WITH_CORRECT_API_KEY;
        String jsonParsed = parser.parseResponse(response);
        boolean condition = Integer.parseInt(jsonParsed) > DEFAULT_TIMESTAMP;
        assertTrue(condition);
    }

    @Test
    public void shouldReturnDefaultTimestampWhenFAILStatus() throws Exception {
        response = CORRECT_RESPONSE_WITH_WRONG_API_KEY;
        String jsonParsed = parser.parseResponse(response);
        boolean condition = Integer.parseInt(jsonParsed) == DEFAULT_TIMESTAMP;
        assertTrue(condition);
    }

    @Test(expected = JSONException.class)
    public void throwJSONExceptionIfWrongResponse() throws Exception {
        response = WRONG_RESPONSE_FORMAT;
        new JSONObject(response);
    }
}