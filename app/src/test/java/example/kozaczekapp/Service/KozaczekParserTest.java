package example.kozaczekapp.Service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;

import static org.junit.Assert.*;

public class KozaczekParserTest {
    private static String URL = "http://www.kozaczek.pl/rss/plotki.xml";
    HttpResponse response;

    @Before
    public void setup()
    {
        HttpClient httpClient = new DefaultHttpClient(); //?
        HttpGet httpGet = new HttpGet(URL);
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void shouldReturnListOfArticles(){
//        //given
//        KozaczekParser parser = new KozaczekParser();
//        //when
//       ArrayList<Article> articles =  parser.parse(response);
//        //then
//        assertNotNull(articles);
//    }
}