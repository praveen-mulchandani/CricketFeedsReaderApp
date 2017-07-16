package downloaddata.com.feeds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import downloaddata.com.feeds.R;

/**
 * Class to display Web view on List Item Click
 */
public class WebViewActivity extends AppCompatActivity {
    private static String sURL;
    private static String sHeadLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());

        Bundle bundle = this.getIntent().getExtras();
        sURL = bundle.getString("Link");
        sHeadLine = bundle.getString("HeadLines");
        if (null != sURL) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(sURL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                shareContent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Method to allow user to share Link on Share Icon Click in Action Bar
     */
    private void shareContent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sHeadLine + "\n" + sURL);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share using"));
    }

    /**
     * To set up the web View
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
