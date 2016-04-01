package example.kozaczekapp.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.R;

/**
 * Adapter Class implementation of ArticleList adapter.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> implements Parcelable {
    ArrayList<Article> listOfArticles = new ArrayList<>();
    private LruCache<String, Bitmap> mLruCache;

    /**
     * Constructor where we initialize LRU cache;
     */
    public ArticleListAdapter(LruCache<String, Bitmap> cache) {
        this.mLruCache = cache;
    }

    /**
     * Method create listOfArticles from parcelable;
     *
     * @param in Parcelable;
     */
    protected ArticleListAdapter(Parcel in) {
        listOfArticles = in.createTypedArrayList(Article.CREATOR);
    }

    /**
     * Creator class for parcelable.
     */
    public static final Creator<ArticleListAdapter> CREATOR = new Creator<ArticleListAdapter>() {
        @Override
        public ArticleListAdapter createFromParcel(Parcel in) {
            return new ArticleListAdapter(in);
        }

        @Override
        public ArticleListAdapter[] newArray(int size) {
            return new ArticleListAdapter[size];
        }
    };

    /**
     * Method to replace ArrayList of Articles.
     *
     * @param list ArrayList of Articles.
     */
    public void replaceListOfArticles(ArrayList<Article> list) {
        this.listOfArticles = list;
    }

    /**
     * @return count of images in Lrucache.
     */
    public int getImagesOnLruCacheCount() {
        return mLruCache.putCount();
    }

    /**
     * This method calls onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder
     * and initializes some private fields to be used by RecyclerView.
     *
     * @return instance of ViewHolder;
     */
    @Override
    public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_element_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of TextView and ProgressBar.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ArticleListAdapter.ViewHolder holder, final int position) {

        if (listOfArticles.size() != 0) {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(listOfArticles.get(position).getLinkToArticle()));
                    v.getContext().startActivity(i);
                }
            });
            holder.mTitle.setText(listOfArticles.get(position).getTitle());
            holder.mDescription.setText(listOfArticles.get(position).getDescription());
            holder.mPubData.setText(listOfArticles.get(position).getPubDate());
            loadImageToImageView(holder, position);
        }
    }

    /**
     * Method used to load image from LruCache.
     *
     * @param holder   instance of ViewHolder.
     * @param position position in list;
     */
    public void loadImageToImageView(ArticleListAdapter.ViewHolder holder, int position) {
        String imageUrl = listOfArticles.get(position).getImage().getImageUrl();
        if (getBitmapFromMemCache(imageUrl) != null) {
            holder.imageView.setImageBitmap(mLruCache.get(imageUrl));
        }
    }

    /**
     * @return number of Tasks in List.
     */
    @Override
    public int getItemCount() {
        return this.listOfArticles.size();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's marshalled representation.
     *
     * @return 0 ?
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  dest The Parcel in which the object should be written
     * @param flags flags Additional flags about how the object should be written. May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listOfArticles);
    }

    /**
     * Used to notify that Image was added to LruCache;
     */
    public void imageUpdate() {
        this.notifyDataSetChanged();
    }

    /**
     * Method to find bitmap in LRU cache;
     *
     * @param key of Bitmap in LRU cache as String
     * @return Bitmap if found if not null;
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    /**
     * class of ViewHolder implementation.
     * Use to remember each element in list.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mDescription;
        public final TextView mPubData;
        public final ImageView imageView;

        /**
         * Constructor where we set reference to layout of progressbar and TextView.
         *
         * @param view instance of view.
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.titleTextView);
            mDescription = (TextView) view.findViewById(R.id.descriptionTextView);
            mPubData = (TextView) view.findViewById(R.id.publicDateTextView);
            imageView = (ImageView) view.findViewById(R.id.articleImage);
        }

        /**
         * @return text as String from titleTextView.
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
