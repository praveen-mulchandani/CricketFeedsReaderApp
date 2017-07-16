package downloaddata.com.feeds.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import downloaddata.com.feeds.R;
import downloaddata.com.feeds.service.DownloadDataService;

/**
 * Class to show a splash Screen on App Startup
 */
public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this, DownloadDataService.class);
        startService(intent);
        //getContentResolver().notifyChange(FeedsListActivity.CONTENT_URI, null, false);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (!isOnline()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setMessage(
                            "Unable to reach server, \nPlease check your Internet connectivity.")
                            .setTitle("CricBuzz News Feeds")
                            .setCancelable(false)
                            .setPositiveButton("Exit",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            finish();
                                        }
                                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    startActivity(new Intent(SplashActivity.this,FeedsListActivity.class));
                    finish();
                }
                // close this activity

            }
        }, SPLASH_TIME_OUT);


    }
}
