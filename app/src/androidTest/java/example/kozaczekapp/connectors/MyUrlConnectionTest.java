package example.kozaczekapp.connectors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MyUrlConnectionTest {

    @Test
    public void shouldReturnResponseAsNull_stringUrlIsNotWellFormatted(){
        //given
        String url = "abcd";
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }

    @Test
    public void shouldReturnResponseAsString_StringUrlIsWellFormatted(){
        //given
        String url = "http://www.kozaczek.pl/rss/plotki.xml";
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNotNull(response);
    }

    @Test
    public void shouldReturnResponseAsNull_stringUrlIsWrong(){
        //given
        String url = null;
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }
    @Test
    public void shouldReturnResponseAsNull_StringUrlIsNotCorrect(){
        //given
        String url = "http://www.kozaczek";
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }
}