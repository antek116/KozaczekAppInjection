package example.kozaczekapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import example.kozaczekapp.authenticator.AuthenticatorActivity;
import example.kozaczekapp.databaseConnection.DatabaseHandler;
import example.kozaczekapp.databaseConnection.RssContract;
import example.kozaczekapp.fragments.ArticleListFragment;
import example.kozaczekapp.imageDownloader.ImageManager;
import example.kozaczekapp.kozaczekItems.Article;
import example.kozaczekapp.preferences.PreferencesActivity;

public class MainActivity extends AppCompatActivity {

    //albo enum
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ANIMATION_MODE_INFINTE, ANIMATION_MODE_ONCE, ANIMATION_MODE_STOP})
    public @interface AnimationMode {}
    public static final int ANIMATION_MODE_INFINTE = 0;
    public static final int ANIMATION_MODE_ONCE = 1;
    public static final int ANIMATION_MODE_STOP = 2;


    public static final String FRAGMENT_KEY = "ArticleListFragmentSaveState";
    private static final String SCREEN_WIDTH = "SCREEN_WIDTH";
    private static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static boolean showNoConnectionMsg = true;
    ArticleListFragment listArticle;
    SwipeRefreshLayout pullToRefresh;

    int screenWidth;
    ImageView image;
    private ObjectAnimator anim;
    private MenuItem refreshMenuItem;

    //FIXME: nie przeplatamy kodu testowego z kodem produkcyjnym
    /**
     * Used to tests
     */
    private static boolean isActivityVisible = false;
    public int startingServiceCounter = 0;
    private OnConnectivityChangeReceiver onConnectivityChangeReceiver = new OnConnectivityChangeReceiver();

    /*************************************************/


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

        //TODO: verify if account exist
        // y -> validate token, open artiles
        // n -> login/register

        initializationOfSaveInstanceState(savedInstanceState);
        initializationOfRefreshItemInMenu(); // TODO: przenieść do onOptionsMenu
        //TODO: unregister
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
                //TODO: używamy fragentów i jednego activity
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.createNewAccount:
                startActivity(new Intent(this, AuthenticatorActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        registerReceiver(onConnectivityChangeReceiver, new IntentFilter(CONNECTIVITY_CHANGE));
        isActivityVisible = true;
    }

    /**
     * Method where we unregister two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    public void onPause() {
        unregisterReceiver(onConnectivityChangeReceiver);
        super.onPause();
        isActivityVisible = false;
    }

    /**
     * Toggles animation of refres icon
     *
     * @param shouldAnimate indicates if animation should be enabled
     * @param mode       elaborate on mode types
     */
    public void toggleAnimation(boolean shouldAnimate, @AnimationMode int mode) {
        if (refreshMenuItem != null) {
            image.setClickable(false);
            pullToRefresh.setEnabled(false);
            pullToRefresh.setRefreshing(false);
            if (shouldAnimate && mode == ANIMATION_MODE_INFINTE) {
                anim.setRepeatCount(ObjectAnimator.INFINITE);
                anim.setRepeatMode(ObjectAnimator.RESTART);
                anim.start();
            } else if (shouldAnimate && mode == ANIMATION_MODE_ONCE) {
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
     *
     * @return true if anim is running
     */
    public boolean isRefreshAnimating(){
        return anim.isRunning();
    }
    /**
     * Method sets the listener for PullToRefresh event.
     * On PullToRefresh when the device is connected to internet the rss data are reloaded.
     */
    private void setupPullToRefreshListener() {
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh); //TODO: move to onCreate
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
        FrameLayout frameLayout = (FrameLayout) inflater.inflate( R.layout.iv_refresh,new LinearLayout(getApplicationContext()), false);
        image = (ImageView) frameLayout.findViewById(R.id.refresh);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkConnection()) {
                    getData();
                } else {
                    toggleAnimation(true, ANIMATION_MODE_INFINTE);
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
        toggleAnimation(true, ANIMATION_MODE_INFINTE);
        requestSync();
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

    class GetArticlesFromDataBase extends AsyncTask<String, String, List<Article>> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<Article> doInBackground(String... params) {
            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
            return db.getAllArticles();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(List<Article> articlesFromDB) {
            listArticle.updateTasksInList(articlesFromDB);
            updateImageToLabCache(listArticle.getImageManager(), articlesFromDB);
            toggleAnimation(false, ANIMATION_MODE_INFINTE);
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
    private void requestSync(){
        // Pass the settings flags by inserting them in a bundle
        AccountManager AccManager = AccountManager.get(this);
        Log.d("BUTTON", "getData: ButtonClicked");
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        Account[] account = AccManager.getAccountsByType(AuthenticatorActivity.ACCOUNT_TYPE);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */

        //TODO: BRAKUJE KONTA w testach, testy leżą i kwiczą
        ContentResolver.requestSync(account[0],RssContract.AUTHORITY, settingsBundle);
    }
}

