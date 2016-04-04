package example.kozaczekapp.Connectors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OkHttpCommunicatorTest {


    @Test
    public void shouldReturnNullAsResponse_stringUrlIsWrong(){
        //given
        String url = "abcd";
        OkHttpCommunicator connection = new OkHttpCommunicator();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }

    @Test
    public void shouldReturnResponseAsString_StringUrlIsWellFormatted(){
        //given
        String url = "http://www.kozaczek.pl/rss/plotki.xml";
        OkHttpCommunicator connection = new OkHttpCommunicator();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNotNull(response);
        assertTrue(response instanceof String);
    }

    @Test
    public void shouldReturnResponseAsNull_StringUrlIsNull(){
        //given
        String url = null;
        OkHttpCommunicator connection = new OkHttpCommunicator();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }
    @Test
    public void shouldReturnResponseAsNull_StringUrlIsNotCorrect(){
        //given
        String url = "http://www.kozaczek";
        OkHttpCommunicator connection = new OkHttpCommunicator();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }
}