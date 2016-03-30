package example.kozaczekapp.ImageDownloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.KozaczekItems.Image;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageManagerTest {

    ImageManager imageManager;
    ArrayList<Article> articles;

    @Before
    public void setUp(){
        imageManager = new ImageManager();
        articles = new ArrayList<>();
    }
    @Test
    public void shouldReturnInstanceOfLruCache(){
        //given
        //when
        LruCache<String,Bitmap> lruCache= imageManager.getLruCache();
        //then
        assertNotNull(lruCache);
    }
   @Test
    public void shouldAddImageToLruCache(){
       //given

       Article article = new Article("123",new Image("http://s1.kozaczek.pl/2016/03/28/tn-100-T1.jpg","123"),"abc","abc","abc");
       articles.add(article);
       //when
       imageManager.addImagesFromArticlesToLruCache(articles);
       LruCache<String,Bitmap> lruCache = imageManager.getLruCache();
       //Watek?
       Bitmap bitmap =  lruCache.get("http://s1.kozaczek.pl/2016/03/28/tn-100-T1.jpg");
       //then      assertNotNull(bitmap);
       //
   }
}