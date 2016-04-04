package example.kozaczekapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.kozaczekapp.DatabaseConnection.DatabaseHandler;
import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.ImageDownloader.ImageManager;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Preferences.PreferencesActivity;
import example.kozaczekapp.Service.KozaczekService;

public class MainActivity extends AppCompatActivity {
    public static final String FRAGMENT_KEY = "ArticleListFragmentSaveState";
    public static final String SERVICE_URL = "http://www.kozaczek.pl/rss/plotki.xml";
    private static final String SCREEN_WIDTH = "SCREEN_WIDTH";
    private static boolean showNoConnectionMsg = true;
    ArticleListFragment listArticle;
    Intent kozaczekServiceIntent;
    SwipeRefreshLayout pullToRefresh;
    OnConnectivityChangeReceiver connectivityChangeReceiver;
    int screenWidth;
    ImageView image;
    private ObjectAnimator anim;
    private IntentFilter filterAdapterArticlesChange = new IntentFilter(KozaczekService.INTENT_FILTER);
    private MenuItem refreshMenuItem;
    private boolean isInternetConnection;
    private ArrayList<Article> articlesFromDB;

    private BroadcastReceiver articlesRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new GetArticlesFromDataBase().execute();
        }
    };

    class GetArticlesFromDataBase extends AsyncTask<String, String, List<Article>> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<Article> doInBackground(String... params) {
            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
            articlesFromDB = (ArrayList<Article>) db.getAllArticles();
            return articlesFromDB;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(List<Article> articlesFromDB) {
            listArticle.updateTasksInList(articlesFromDB);
            updateImageToLabCache(listArticle.getImageManager(), articlesFromDB);
            startOrStopRefreshingAnimation(false, 0);
        }
    }

    private void updateImageToLabCache(ImageManager imageManager, List<Article> articles) {
        imageManager.addImagesFromArticlesToLruCache(articles);
    }

    public Intent getKozaczekServiceIntent() {
        return kozaczekServiceIntent;
    }

    /**
     * Methods where we initialize service.
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
        switch (item.getItemId()) {
            case R.id.ulrConnectionOptions:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivity(i);

                break;
            default:
                break;
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
        outState.putInt(SCREEN_WIDTH, screenWidth);
    }

    /**
     * Method where we register two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setupPullToRefreshListener();
        this.registerReceiver(articlesRefreshReceiver, filterAdapterArticlesChange);
        IntentFilter connectivityChangefilter =
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        connectivityChangeReceiver = new OnConnectivityChangeReceiver();
        registerReceiver(connectivityChangeReceiver, connectivityChangefilter);
    }

    /**
     * Method where we unregister two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    public void onPause() {
        unregisterReceiver(articlesRefreshReceiver);
        unregisterReceiver(connectivityChangeReceiver);
        super.onPause();
    }

    /**
     * Method starting chosen by kind refreshing animation.
     *
     * @param refreshing true if wanna start animation, false if wanna stop animation, animation is stoping by dooing last circle.
     * @param kind       1 - Infinite animation, 2 - one loop animation.
     */
    public void startOrStopRefreshingAnimation(boolean refreshing, int kind) {
        if (refreshMenuItem != null) {
            image.setClickable(false);
            pullToRefresh.setEnabled(false);
            pullToRefresh.setRefreshing(false);
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
                anim.setRepeatCount(0);
                image.setClickable(true);
                pullToRefresh.setRefreshing(false);
                pullToRefresh.setEnabled(true);
            }
        }
    }

    /**
     * @return true if is internet connection yet, false if not.
     */
    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Method sets the listener for PullToRefresh event.
     * On PullToRefresh when the device is connected to internet the rss data are reloaded.
     */
    private void setupPullToRefreshListener() {
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        if (pullToRefresh != null) {
            pullToRefresh.setRefreshing(false);
            pullToRefresh.setEnabled(true);
        }
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkNetworkConnection()) {
                    pullToRefresh.setRefreshing(true);
                    pullToRefresh.setEnabled(false);
                    if (image != null) {
                        image.setClickable(false);
                    }
                    getData();
                }
            }
        });
    }

    private void initializationOfSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            listArticle = new ArticleListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, listArticle).commit();
            isInternetConnection = checkNetworkConnection();
            screenWidth = getScreenWidth();
            if (isInternetConnection) {
                getData();
            }
        } else {
            listArticle = (ArticleListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            screenWidth = savedInstanceState.getInt(SCREEN_WIDTH);
        }
    }

    private void initializationOfRefreshItemInMenu() {

        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.iv_refresh, null, false);
        image = (ImageView) frameLayout.findViewById(R.id.refresh);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkConnection()) {
                    startOrStopRefreshingAnimation(true, 1);
                    startService(getKozaczekServiceIntent());
                } else {
                    startOrStopRefreshingAnimation(true, 2);
                    String message = getResources().getString(R.string.no_internet_connection);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        anim = ObjectAnimator.ofFloat(image, "rotation", 0f, 360f).setDuration(1000);

    }

    /**
     * Method getData starts the animation of refresh button and starts service which downloads rss feeds
     */
    private void getData() {
        startOrStopRefreshingAnimation(true, 1);
        startService(getKozaczekServiceIntent());
    }

    /**
     * Method displays message when the device is not connected to internet
     */
    private void showInternetNoConnectionMsg() {
        String message = getResources().getString(R.string.no_internet_connection);
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        if (showNoConnectionMsg) {
            toast.show();
            showNoConnectionMsg = false;
        }
    }

    /**
     * Receiver OnConnectivityChangeReceiver listens of the state of connection has changed.
     * If the device disconnects the suitable message is shown and PullToRefresh is disabled.
     * On connection the rss feeds are loaded.
     */
    private class OnConnectivityChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = checkNetworkConnection();
            if (isConnected) {
                if (!isInvertedScreen()) {
                    getData();
                    showNoConnectionMsg = true;
                }
            } else {
                pullToRefresh.setEnabled(false);
                pullToRefresh.setRefreshing(false);
                showInternetNoConnectionMsg();
            }
        }
    }

    private boolean isInvertedScreen() {
        int newWidth = getScreenWidth();
        if (screenWidth != newWidth) {
            screenWidth = newWidth;
            return true;
        } else {
            return false;
        }
    }

    private int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
}

