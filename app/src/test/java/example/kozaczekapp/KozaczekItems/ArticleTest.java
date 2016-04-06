package example.kozaczekapp.kozaczekItems;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArticleTest {
    private static final String PUB_DATE = "Published at";
    private static final String IMAGE_URL = "http://s1.kozaczek.pl/2016/03/28/tn-100-T1.jpg";
    private static final String IMAGE_SIZE = "1234";
    private static final String LINK_GUIDE = "linkGuide";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    Article article;
    @Before
    public void setUp() throws Exception {
        article = new Article(PUB_DATE,new Image(IMAGE_URL,IMAGE_SIZE),LINK_GUIDE,TITLE,DESCRIPTION);
    }
    @Test
    public void shouldReturnImageFromArticle(){
        //given
        //when
        Image image = article.getImage();
        //then
        assertNotNull(image);
    }


}