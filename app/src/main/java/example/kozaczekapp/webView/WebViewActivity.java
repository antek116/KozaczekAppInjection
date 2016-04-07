package example.kozaczekapp.webView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import example.kozaczekapp.R;
import example.kozaczekapp.fragments.ArticleListAdapter;

public class WebViewActivity extends AppCompatActivity {

    private WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        browser = (WebView) findViewById(R.id.webview);

        String url;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                url = null;
            } else {
                url = extras.getString(ArticleListAdapter.LINK_URL_KEY);
            }
        } else {
            url = savedInstanceState.getString(ArticleListAdapter.LINK_URL_KEY);
        }
        if (browser != null) {
            browser.loadUrl(url);
            // Force links and redirects to open in the WebView instead of in a browser
            browser.setWebViewClient(new WebViewClient());
        }

    }

    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }


}
