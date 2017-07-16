package downloaddata.com.feeds.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import downloaddata.com.feeds.R;
import downloaddata.com.feeds.asynctask.BitMapWorkerTask;
import downloaddata.com.feeds.model.NewsItem;

public class FeedsDetailFragment extends Fragment {

    private NewsItem newsItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView thumb = (ImageView)getActivity().findViewById(R.id.featuredImg);
        new BitMapWorkerTask(getActivity(),thumb).execute(newsItem.getmPhoto());

        TextView title = (TextView) getActivity(). findViewById(R.id.title);
        title.setText(newsItem.getmHeadline());

        TextView htmlTextView = (TextView) getActivity().findViewById(R.id.content);
        htmlTextView.setText(newsItem.getmStory()/*Html.fromHtml(newsItem.getmStory(), null, null)*/);
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
        sendIntent.putExtra(Intent.EXTRA_TEXT, newsItem.getmHeadline() + "\n" + newsItem.getmLink());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share using"));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsItem = (NewsItem) getArguments().getSerializable("Data");
        Log.d("Tag", newsItem.getmHeadline());
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Full Story");
        }
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_feed_details,container,false);

    }
}
