package example.kozaczekapp;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.ImageDownloader.ImageManager;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Preferences.AppPreferences;
import example.kozaczekapp.Service.KozaczekService;
import example.kozaczekapp.Service.MyOnClickListener;

public class MainActivity extends AppCompatActivity {
    public static final String FRAGMENT_KEY = "ArticleListFragmentSaveState";
    public static final String SERVICE_URL = "http://www.kozaczek.pl/rss/plotki.xml";
    ArticleListFragment listArticle;
    Intent kozaczekServiceIntent;
    ImageView image;
    private ObjectAnimator anim;
    private IntentFilter filterAdapterArticlesChange = new IntentFilter(KozaczekService.INTENT_FILTER);
    private MenuItem refreshMenuItem;
    private BroadcastReceiver articlesRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Article> articles = intent.getParcelableArrayListExtra(ArticleListFragment.PARCELABLE_ARTICLE_ARRAY_KEY);
            listArticle.updateTasksInList(articles);
            updateImageToLabCache(listArticle.getImageManager(), articles);
            startOrStopRefreshingAnimation(false, 0);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void updateImageToLabCache(ImageManager imageManager, ArrayList<Article> articles) {
        imageManager.addImagesFromArticlesToLruCache(articles);
    }

    public Intent getKozaczekServiceIntent() {
        return kozaczekServiceIntent;
    }

    private boolean isInternetConnection;
    private IntentFilter filterForInternetConnectionChange = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private BroadcastReceiver networkConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // FIXME wzorzec obserwator do informowania o zmianie stanu
            isInternetConnection = checkNetworkConnection();
        }
    };

    /**
     * Methods where we initialize servis.
     *
     * @param savedInstanceState saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kozaczekServiceIntent = new Intent(MainActivity.this, KozaczekService.class);
        kozaczekServiceIntent.putExtra(KozaczekService.URL, SERVICE_URL);
        initializationOfSaveInstanceState(savedInstanceState);
        initializationOfRefreshItemInMenu();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        refreshMenuItem = menu.findItem(R.id.item_refresh);
        refreshMenuItem.setActionView(image);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ulrConnectionOptions :
                Intent i = new Intent(this, AppPreferences.class);
                startActivity(i);

                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * MEthod use to save instance of fragment.
     *
     * @param outState bundle of saves states.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, listArticle);
    }

    /**
     * Method where we register two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(articlesRefreshReceiver, filterAdapterArticlesChange);
        registerReceiver(networkConnectionReceiver, filterForInternetConnectionChange);
    }

    /**
     * Method where we unregister two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    public void onPause() {
        unregisterReceiver(articlesRefreshReceiver);
        unregisterReceiver(networkConnectionReceiver);
        super.onPause();
    }

    /**
     * Method starting choosen by kind refreshing animation.
     *
     * @param refreshing true if wanna start animation, false if wanna stop animation, animation is stoping by dooing last circle.
     * @param kind       1 - Infinite animation, 2 - one loop animation.
     */
    public void startOrStopRefreshingAnimation(boolean refreshing, int kind) {
        if (refreshMenuItem != null) {
            image.setClickable(false);
            if (refreshing && kind == 1) {
                anim.setRepeatCount(ObjectAnimator.INFINITE);
                anim.setRepeatMode(ObjectAnimator.RESTART);
                anim.start();
            } else if (refreshing && kind == 2) {
                anim.setRepeatCount(1);
                anim.setRepeatMode(ObjectAnimator.REVERSE);
                anim.start();
                image.setClickable(true);
            } else {
                anim.setRepeatCount(1);
                anim.start();
                image.setClickable(true);
            }
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initializationOfSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            listArticle = new ArticleListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, listArticle).commit();
            isInternetConnection = checkNetworkConnection();
            if (isInternetConnection) {
                startOrStopRefreshingAnimation(true, 1);
                startService(kozaczekServiceIntent);
            } else {
                Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        } else {
            listArticle = (ArticleListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        }
    }

    private void initializationOfRefreshItemInMenu() {

        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.iv_refresh, null);
        image = (ImageView) frameLayout.findViewById(R.id.refresh);

        //FIXME iv.setOnClickListener();
//        frameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkNetworkConnection()) {
//                    startOrStopRefreshingAnimation(true, 1);
//                    startService(getKozaczekServiceIntent());
//                } else {
//                    startOrStopRefreshingAnimation(true, 2);
//                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        image.setOnClickListener(new MyOnClickListener(this));
        anim = ObjectAnimator.ofFloat(image, "rotation", 0f, 360f);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://example.kozaczekapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://example.kozaczekapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
