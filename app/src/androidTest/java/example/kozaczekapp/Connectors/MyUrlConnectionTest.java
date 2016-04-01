package example.kozaczekapp.Connectors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MyUrlConnectionTest {

    @Test
    public void shouldReturnNullAsXml_stringUrlIsWrong(){
        //given
        String url = "abcd";
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }

    @Test
    public void shouldReturnXmlAsString_StringUrlIsWellFormated(){
        //given
        String url = "http://www.kozaczek.pl/rss/plotki.xml";
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNotNull(response);
    }

    @Test
    public void shouldReturnXmlAsNull_IfStringUrlIsNull(){
        //given
        String url = null;
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }
    @Test
    public void shouldReturnXmlAsNull_StringUrlIsNotCorrect(){
        //given
        String url = "http://www.kozaczek";
        MyUrlConnection connection = new MyUrlConnection();
        //when
        String response =  connection.getResponse(url);
        //then
        assertNull(response);
    }
}