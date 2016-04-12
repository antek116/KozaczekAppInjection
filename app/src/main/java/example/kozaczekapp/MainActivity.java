package example.kozaczekapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.kozaczekapp.authenticator.AccountKeyConstants;
import example.kozaczekapp.authenticator.AuthenticatorActivity;
import example.kozaczekapp.databaseConnection.RssContract;
import example.kozaczekapp.fragments.ArticleListFragment;
import example.kozaczekapp.imageDownloader.ImageManager;
import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.preferences.PreferencesActivity;

public class MainActivity extends AppCompatActivity {
    public static final String FRAGMENT_KEY = "ArticleListFragmentSaveState";
    private static final String SCREEN_WIDTH = "SCREEN_WIDTH";
    private static final int NO_INTERNET_CONNECTION_KIND = 2;
    private static final int START_ANIMATE_KIND = 1;
    private static final int STOP_ANIMATION_KIND = 0;
    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL = SECONDS_PER_MINUTE * SYNC_INTERVAL_IN_MINUTES;
    private static boolean showNoConnectionMsg = true;
    private static boolean isActivityVisible = false;
    public int startingServiceCounter = 0;
    ArticleListFragment listArticle;
    SwipeRefreshLayout pullToRefresh;
    OnConnectivityChangeReceiver connectivityChangeReceiver;
    private int screenWidth;
    private ImageView image;
    private ObjectAnimator anim;
    private MenuItem refreshMenuItem;
    private Account[] accounts;

    public static boolean getActivityVisibilityState() {
        return isActivityVisible;
    }


    private void updateImageToLabCache(ImageManager imageManager, List<Article> articles) {
        imageManager.addImagesFromArticlesToLruCache(articles);
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
        initializationOfRefreshItemInMenu();
        AccountManager accountManager = AccountManager.get(this);
        if (accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE).length < 1) {
            Intent i = new Intent(this, AuthenticatorActivity.class);
            i.putExtra(AccountKeyConstants.ARG_CLICKED_FROM_SETTINGS, false);
            this.startActivity(i);
        } else {
            syncAdapterRefreshingSetup();
            initializationOfSaveInstanceState(savedInstanceState);

            getContentResolver().registerContentObserver(RssContract.CONTENT_URI, true,
                    new ContentObserver(new Handler()) {
                        @Override
                        public boolean deliverSelfNotifications() {
                            return super.deliverSelfNotifications();
                        }

                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                        }

                        @Override
                        public void onChange(boolean selfChange, Uri uri) {
                            new GetArticlesFromDataBase().execute();
                            super.onChange(selfChange, uri);

                        }
                    });

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Log.d("MaunActivity", "onCreate: " + prefs.getBoolean("emailPref", false));
        }
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
        Intent i;
        switch (item.getItemId()) {
            case R.id.ulrConnectionOptions:
                i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                break;
            case R.id.logIn:
                i = new Intent(this, AuthenticatorActivity.class);
                i.putExtra(AccountKeyConstants.ARG_CLICKED_FROM_SETTINGS, false);
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
        IntentFilter connectivityChangefilter =
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        connectivityChangeReceiver = new OnConnectivityChangeReceiver();
        registerReceiver(connectivityChangeReceiver, connectivityChangefilter);
        isActivityVisible = true;
    }

    /**
     * Method where we unregister two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    public void onPause() {
        unregisterReceiver(connectivityChangeReceiver);
        super.onPause();
        isActivityVisible = false;
    }

    /**
     * Method starting chosen by kind refreshing animation.
     *
     * @param refreshing true if wanna start animation, false if wanna stop animation, animation is stoping by dooing last circle.
     * @param kind       1 - Infinite animation, 2 - one loop animation.
     */
    public void refreshingAnimationSetUp(boolean refreshing, int kind) {
        if (refreshMenuItem != null) {
            image.setClickable(false);
            pullToRefresh.setEnabled(false);
            pullToRefresh.setRefreshing(false);
            if (refreshing && kind == START_ANIMATE_KIND) {
                anim.setRepeatCount(ObjectAnimator.INFINITE);
                anim.setRepeatMode(ObjectAnimator.RESTART);
                anim.start();
            } else if (refreshing && kind == NO_INTERNET_CONNECTION_KIND) {
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
     * @return true if anim is running
     */
    public boolean isRefreshAnimating() {
        return anim.isRunning();
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
            screenWidth = getScreenWidth();
        } else {
            listArticle = (ArticleListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            screenWidth = savedInstanceState.getInt(SCREEN_WIDTH);
        }
    }

    private void initializationOfRefreshItemInMenu() {

        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.iv_refresh, new LinearLayout(getApplicationContext()), false);
        image = (ImageView) frameLayout.findViewById(R.id.refresh);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkConnection()) {
                    getData();
                } else {
                    refreshingAnimationSetUp(true, NO_INTERNET_CONNECTION_KIND);
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
        refreshingAnimationSetUp(true, START_ANIMATE_KIND);
        syncAdapterRequest();
        startingServiceCounter++;
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

    private void syncAdapterRequest() {
        // Pass the settings flags by inserting them in a bundle
        Log.d("BUTTON", "getData: ButtonClicked");
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */

        if (accounts.length > 0) {
            ContentResolver.requestSync(accounts[0], RssContract.AUTHORITY, settingsBundle);
        }
    }

    /**
     * Turn on periodic syncing
     */
    private void syncAdapterRefreshingSetup() {
        AccountManager AccManager = AccountManager.get(this);
        accounts = AccManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, false);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_DO_NOT_RETRY, false);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
        ContentResolver.setMasterSyncAutomatically(true);
        ContentResolver.addPeriodicSync(
                accounts[0],
                RssContract.AUTHORITY,
                settingsBundle,
                SYNC_INTERVAL);
    }

    /**
     * @return list of all articles from db
     */

    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(RssContract.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.fromCursor(cursor);
                // Adding article to list
                articleList.add(article);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return articleList;
    }

    class GetArticlesFromDataBase extends AsyncTask<String, String, List<Article>> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<Article> doInBackground(String... params) {
            return getAllArticles();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(List<Article> articlesFromDB) {
            listArticle.updateTasksInList(articlesFromDB);
            updateImageToLabCache(listArticle.getImageManager(), articlesFromDB);
            refreshingAnimationSetUp(false, STOP_ANIMATION_KIND);
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
}

