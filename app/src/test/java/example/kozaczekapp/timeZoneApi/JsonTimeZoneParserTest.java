package example.kozaczekapp.timeZoneApi;

import org.json.JSONException;
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

    @Test
    public void shouldReturnNotNullResponse() throws Exception {
        //given
        response = CORRECT_RESPONSE_WITH_CORRECT_API_KEY;
        //when
        String jsonParsed = parser.parseResponse(response);
        //then
        assertNotNull(jsonParsed);
    }

    @Test
    public void shouldReturnFAILStatusWhenWrongAPI() throws Exception {
        //given
        String STATUS_FAIL = "FAIL";
        response = CORRECT_RESPONSE_WITH_WRONG_API_KEY;
        //when
        String actualStatus = JsonTimeZoneParser.getResponseStatus(response);
        //then
        assertEquals(STATUS_FAIL, actualStatus);
    }

    @Test
    public void shouldReturnOKStatusWhenCorrectAPI() throws Exception {
        //given
        response = CORRECT_RESPONSE_WITH_CORRECT_API_KEY;
        String STATUS_OK = "OK";
        //when
        String actualStatus = JsonTimeZoneParser.getResponseStatus(response);
        //then
        assertEquals(STATUS_OK, actualStatus);
    }

    @Test
    public void shouldReturnNumberGreaterThanDefaultTimestampWhenOKStatus() throws Exception {
        //given
        response = CORRECT_RESPONSE_WITH_CORRECT_API_KEY;
        //when
        String jsonParsed = parser.parseResponse(response);
        boolean condition = Integer.parseInt(jsonParsed) > DEFAULT_TIMESTAMP;
        //then
        assertTrue(condition);
    }

    @Test
    public void shouldReturnDefaultTimestampWhenFAILStatus() throws Exception {
        //given
        response = CORRECT_RESPONSE_WITH_WRONG_API_KEY;
        //when
        String jsonParsed = parser.parseResponse(response);
        boolean condition = Integer.parseInt(jsonParsed) == DEFAULT_TIMESTAMP;
        //then
        assertTrue(condition);
    }

    @Test(expected = JSONException.class)
    public void throwJSONExceptionIfWrongResponse() throws Exception {
        //given
        response = WRONG_RESPONSE_FORMAT;
        //when
        parser.parseResponse(response);
        //then JSONException
    }
}