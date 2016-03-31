package example.kozaczekapp.Service;

import android.support.test.runner.AndroidJUnit4;

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

    @Test
    public void shouldReturnNull_ResponseIsWrongFormatted() throws Exception{
        //given
        String response = "abcdefghijkal";
        Parser parser = new Parser(response);
        //when
        articles = parser.parse();
        //then
        assertNull(articles);
    }
    @Test
    public void shouldReturnArrayOfArticles_ResponseIsWellFormatted() throws Exception {
        //given
        String response = "<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>\n" +
                "<rss version=\"2.0\">\n" +
                "<channel>\n" +
                "<title>Najnowsze Plotki</title>\n" +
                "<link>http://www.kozaczek.pl/</link>\n" +
                "<description>Kozaczek.pl - najnowsze plotki</description>\n" +
                "<language>pl</language>\n" +
                "<pubDate>Thu, 31 Mar 2016 19:30:02 +0200</pubDate>\n" +
                "<lastBuildDate>Thu, 31 Mar 2016 19:30:02 +0200</lastBuildDate>\n" +
                "<docs>http://www.kozaczek.pl</docs>\n" +
                "<generator>custom Plotki</generator>\n" +
                "<managingEditor>admin@kozaczek.pl</managingEditor>\n" +
                "<webMaster>admin@kozaczek.pl</webMaster>\n" +
                "<item>\n" +
                "<enclosure type=\"image/jpeg\" url=\"http://s1.kozaczek.pl/2016/03/31/tn1--30-T1.jpg\" length=\"6502\" />\n" +
                "<guid>http://www.kozaczek.pl/plotka.php?id=69416&amp;utm_medium=rss</guid>\n" +
                "<category>Skandale</category>\n" +
                "<title>Magda Narożna z Pięknych i Młodych została pobita na koncercie!</title>\n" +
                "<link>http://www.kozaczek.pl/plotka.php?id=69416&amp;utm_medium=rss</link>\n" +
                "<description><![CDATA[\"To największe upokorzenie w moim życiu!\"]]></description>\n" +
                "<pubDate>Thu, 31 Mar 2016 17:07:32 +0200</pubDate>\n" +
                "</item>" +
                "</channel>" +
                "</rss>";
        Parser parser = new Parser(response);
        //when
        articles = parser.parse();
        //then
        assertNotNull(articles);
    }
}