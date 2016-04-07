package example.kozaczekapp.imageDownloader;


import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.LruCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.kozaczekItems.Image;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class ImageManagerTest {
    ImageManager manager;
    @Before
    public void setup(){
        manager = ImageManager.getInstance();
    }

    @After
    public void tearDown(){
        manager = null;
    }


    @Test
    public void shouldReturnSingletonInstanceOfImageManager(){
        //given
        //when
        ImageManager manager1 = ImageManager.getInstance();
        //then
        assertEquals(manager, manager1);
    }

    @Test
    public void shouldReturnInstanceOfLruCache(){
        //given
        //when
        LruCache<String,Bitmap> lruCache = manager.getLruCache();
        //then
        assertNotNull(lruCache);
    }

    @Test
    public void shouldAddImageFromArticleToLruCache(){
        //given
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(new Article("ABC", new Image("http://s1.kozaczek.pl/2016/03/31/tn1--30-T1.jpg","123"), "ABC", "ABC", "ABC"));
        //when
        manager.addImagesFromArticlesToLruCache(articles);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count = manager.getLruCache().putCount();
        //then
        assertEquals(1, count);
    }

    @Test
    public void shouldNotAddTheSameImageTwoTimesToLruCache(){
        //given
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(new Article("ArticleOne", new Image("http://s1.kozaczek.pl/2016/03/31/tn1--30-T1.jpg","123"), "ABC", "ABC", "ABC"));
        articles.add(new Article("ArticleTwoWithTheSameImage", new Image("http://s1.kozaczek.pl/2016/03/31/tn1--30-T1.jpg","123"), "ABC", "ABC", "ABC"));
        //when
        manager.addImagesFromArticlesToLruCache(articles);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count = manager.getLruCache().putCount();
        //then
        assertEquals(1,count);
    }
}