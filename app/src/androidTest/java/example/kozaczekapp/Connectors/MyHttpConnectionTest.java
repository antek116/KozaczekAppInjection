package example.kozaczekapp.Connectors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MyHttpConnectionTest {


    @Test
    public void shouldReturnResponseAsNull_stringUrlIsWrong() {
        //given
        String url = "abcd";
        MyHttpConnection connection = new MyHttpConnection();
        //when
        String response = connection.getResponse(url);
        //then
        assertNull(response);
    }

    @Test
    public void shouldReturnResponseAsString_StringUrlIsWellFormatted() {
        //given
        String url = "http://www.kozaczek.pl/rss/plotki.xml";
        MyHttpConnection connection = new MyHttpConnection();
        //when
        String response = connection.getResponse(url);
        //then
        assertNotNull(response);
        assertTrue(response instanceof String);
    }

    @Test
    public void shouldReturnXmlAsNull_IfStringUrlIsNull() {
        //given
        String url = null;
        MyHttpConnection connection = new MyHttpConnection();
        //when
        String response = connection.getResponse(url);
        //then
        assertNull(response);
    }

    @Test
    public void shouldReturnXmlAsNull_StringUrlIsNotCorrect() {
        //given
        String url = "http://www.kozaczek";
        MyHttpConnection connection = new MyHttpConnection();
        //when
        String response = connection.getResponse(url);
        //then
        assertNull(response);
    }
}