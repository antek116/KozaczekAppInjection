package example.kozaczekapp.Service;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ParserTest  {
    ArrayList<Article> articles;


    @Test
    public void shouldReturnNull_ResponseIsNull() throws Exception{
        //given
        String response = null;
        Parser parser = new Parser(response);
        //when
        articles = parser.parse();
        //then
        assertNull(articles);
    }
}