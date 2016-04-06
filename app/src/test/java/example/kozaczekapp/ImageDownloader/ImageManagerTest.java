package example.kozaczekapp.imageDownloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import example.kozaczekapp.kozaczekItems.Article;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageManagerTest {

    ImageManager imageManager;
    ArrayList<Article> articles;

    @Before
    public void setUp(){
        imageManager = ImageManager.getInstance();
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
}