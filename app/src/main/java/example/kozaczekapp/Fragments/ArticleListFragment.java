package example.kozaczekapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import example.kozaczekapp.ImageDownloader.ImageManager;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.R;

/**
 *  Fragment class implements List of Articles in UI.
 *
 */
public class ArticleListFragment extends Fragment implements Observer{
    ArticleListAdapter adapter;
    public static final String PARCELABLE_ARTICLE_ARRAY_KEY = "FragmentParcelable";
    public static final String PARCELABLE_ADAPTER_KEY = "Adapter_Parcelable";
    private static final String PARCELABLE_IMAGE_MANAGER_KEY = "ImageManagerKeyParcelable";
    private ImageManager imageManager;


    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical
     * fragments can return null (which is the default implementation). This will be called between onCreate(Bundle)
     * and onActivityCreated(Bundle).
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view
     * @param savedInstanceState  If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initialSaveInstance(savedInstanceState);

        View view = inflater.inflate(R.layout.article_list_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    /**
     * Method to replace all articles in adapter for new articles
     * and notify it to adapter;
     * @param articles ArrayList of articles.
     */
    public void updateTasksInList(ArrayList<Article> articles){
        adapter.replaceListOfArticles(articles);
        adapter.notifyDataSetChanged();
    }
    public ImageManager getImageManager(){
        return this.imageManager;
    }
    /**
     * Called to ask the fragment to save its current dynamic state, so it can later be reconstructed
     * in a new instance of its process is restarted. If a new instance of the fragment later needs
     * to be created, the data you place in the Bundle here will be available in the Bundle given
     * to onCreate(Bundle),onCreateView(LayoutInflater, ViewGroup, Bundle), and onActivityCreated(Bundle)
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ArticleListFragment.PARCELABLE_ADAPTER_KEY, adapter);
        outState.putParcelable(PARCELABLE_IMAGE_MANAGER_KEY,imageManager);
    }

    @Override
    public void update(Observable observable, Object data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.imageUpdate();
            }
        });
    }
    private void initialSaveInstance(Bundle savedInstanceState){
        if(savedInstanceState != null){
            adapter = savedInstanceState.getParcelable(PARCELABLE_ADAPTER_KEY);
            imageManager = savedInstanceState.getParcelable(PARCELABLE_IMAGE_MANAGER_KEY);
        }else {
            imageManager= ImageManager.getInstance();
            imageManager.addObserver(this);
            adapter = new ArticleListAdapter(imageManager.getLruCache());
        }
    }
}
