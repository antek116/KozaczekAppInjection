package example.kozaczekapp.imageDownloader2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;

/**
 * Class used to load image from Url.
 */
public class ImageLoad extends Observable implements Runnable {
    String imageUrl;
    LruCache<String, Bitmap> mLruCache;
    ImageManager imageManager;

    /**
     * Constructor of class.
     *
     * @param imageUrl     url to image as a string.
     * @param mLruCache    instance of Lrucache.
     * @param imageManager instance of image manager class.
     */
    public ImageLoad(String imageUrl, LruCache<String, Bitmap> mLruCache, ImageManager imageManager) {
        this.imageUrl = imageUrl;
        this.mLruCache = mLruCache;
        this.imageManager = imageManager;
    }

    /**
     * Method used to load image from url.
     */
    @Override
    public void run() {
        loadImageFromUrl(imageUrl);
        setChanged();
        notifyObservers();
//        deleteObservers();
    }

    private void loadImageFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            resize(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap resize(Bitmap image) {
        Bitmap bitmapResized = Bitmap.createScaledBitmap(image, 80, 80, false);
        if (mLruCache.get(imageUrl) == null) {
            mLruCache.put(imageUrl, bitmapResized);
        }
        return bitmapResized;
    }

}
