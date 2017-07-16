package downloaddata.com.feeds.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import downloaddata.com.feeds.R;
import downloaddata.com.feeds.fragment.FeedListFragment;

/**
 * Activity to Display the News Feeds In List View
 * Consists of Swipe Refresh Layout to Refresh Data
 */
public class FeedsListActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_main);
        FeedListFragment feedListFragment = new FeedListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flcontent,feedListFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG","Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "Stop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;


    }


}
