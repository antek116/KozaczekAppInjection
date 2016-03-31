package example.kozaczekapp.ImageDownloader;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LruCache;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import example.kozaczekapp.KozaczekItems.Article;

/**
 * Implementation of Class ImageManager;
 * Class used to manage all image that have been or has been downloaded.
 */
public class ImageManager extends Observable implements Observer, Parcelable {

    private LruCache<String, Bitmap> mLruCache;
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private static ImageManager instance = null;

    /**
     * Constructor of imageManager where we initialize LruCache;
     */
    @Singleton
    protected ImageManager() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            /**
             * Method to return size of item in LRU cache;
             * @param key as String.
             * @param bitmap instance of Bitmap
             * @return size of bitmap in KB;
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    protected ImageManager(Parcel in) {
    }

    public static final Creator<ImageManager> CREATOR = new Creator<ImageManager>() {
        @Override
        public ImageManager createFromParcel(Parcel in) {
            return new ImageManager(in);
        }

        @Override
        public ImageManager[] newArray(int size) {
            return new ImageManager[size];
        }
    };

    /**
     * @return instance of ImageManager.
     */
    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
            return instance;
        } else {
            return instance;
        }
    }

    /**
     * @return instance of LruCache;
     */
    public LruCache<String, Bitmap> getLruCache() {
        return this.mLruCache;
    }

    /**
     * method add images from Articles to LruCache.
     *
     * @param articleArrayList Articles as ArrayList.
     */
    public void addImagesFromArticlesToLruCache(ArrayList<Article> articleArrayList) {
        for (Article article : articleArrayList) {

            String imageUrl = article.getImage().getImageUrl();
            boolean noImageInLruCache = (!(imageUrl.isEmpty()) && mLruCache.get(imageUrl) == null);
            if (noImageInLruCache) {
                downloadBitmap(imageUrl);
            }
        }
    }

    /**
     * This method is called whenever the observed object is changed.
     * An application calls an Observable object's notifyObservers method to
     * have all the object's observers notified of the change.
     *
     * @param observable the observable object.
     * @param data       argument passed to the notifyObservers method.
     */
    @Override
    public void update(Observable observable, Object data) {
        setChanged();
        notifyObservers();
    }

    private void downloadBitmap(String imageUrl) {
        ImageLoad imageLoad = new ImageLoad(imageUrl, mLruCache, this);
        imageLoad.addObserver(this);
        executor.execute(imageLoad);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
