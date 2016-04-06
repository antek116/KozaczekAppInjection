package example.kozaczekapp.fragments;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.kozaczekItems.Image;

import static org.junit.Assert.*;

public class ArticleListAdapterTest {

    private ArrayList<Article> articles;
    private LruCache<String,Bitmap> lruCache;
    ArticleListAdapter adapter;
    @Before
    public void setup(){
        articles = new ArrayList<>();
        adapter = new ArticleListAdapter(lruCache);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Test
    public void ShouldReturnZeroAsCountItemInList(){
        //given
        //when
        int count = adapter.getItemCount();
        //then
        assertEquals(0,count);

    }
    @Test
    public void shouldAddOneArticleToAdapter(){
        //given
        Article article = new Article("123",new Image("Abc","123"),"abc","abc","abc");
        articles.add(article);
        adapter.replaceListOfArticles(articles);
        //when
        int count = adapter.getItemCount();
        //then
        assertEquals(1,count);
    }
    @Test
    public void shouldReturnNullFromBitmapThatNotExist(){
        //given
        //when
        Bitmap bitmap = lruCache.get("Abc");
        //then
        assertNull(bitmap);
    }
}