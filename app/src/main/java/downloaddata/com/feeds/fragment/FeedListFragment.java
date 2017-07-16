package downloaddata.com.feeds.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import downloaddata.com.feeds.R;
import downloaddata.com.feeds.adapter.FeedsCustomAdapter;
import downloaddata.com.feeds.model.NewsItem;

/**
 * Feed List Fragment
 */
public class FeedListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,SwipeRefreshLayout.OnRefreshListener {

    public static final String PROVIDER_NAME = "com.download.service";
    public static final String URL = "content://" + PROVIDER_NAME + "/CricketDb";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final int CONTACT_LOADER_ID = 78;
    private ListView mFeedsListView;
    private SwipeRefreshLayout mSwipeContainer;
    private Cursor mCursor;
    FragmentManager manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.activity_feeds_list_fragment, container, false);
        mSwipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Top Healines");
        }

        mFeedsListView = (ListView)view.findViewById(R.id.feeds_listview);
        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home, menu);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(
                R.color.swiperedcolor,
                R.color.swipegreencolor,
                R.color.swipeyellowcolor,
                R.color.swipebluecolor);

        getLoaderManager().initLoader(CONTACT_LOADER_ID, null, this);
         manager = getActivity().getSupportFragmentManager();
        mFeedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCursor = (Cursor) parent.getItemAtPosition(position);
                String Link = mCursor.getString(mCursor.getColumnIndex("LINK"));
                String HeadLine = mCursor.getString(mCursor.getColumnIndex("HeadLines"));
                String Story = mCursor.getString(mCursor.getColumnIndex("Story"));
                String Photo = mCursor.getString(mCursor.getColumnIndex("PHOTO"));

                NewsItem item = new NewsItem();
                item.setmStory(Story);
                item.setmHeadline(HeadLine);
                item.setmPhoto(Photo);
                item.setmLink(Link);
                FragmentTransaction transaction = manager.beginTransaction();

                FeedsDetailFragment feedsDetailFragment = new FeedsDetailFragment();

                Bundle dataBundle = new Bundle();
                dataBundle.putSerializable("Data", item);
                feedsDetailFragment.setArguments(dataBundle);

                transaction.replace(R.id.flcontent, feedsDetailFragment).addToBackStack(null);

                transaction.commit();

            }
        });
    }

    //Setting up the Loader
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
    /*    mProgressDialog = new ProgressDialog(FeedsListActivity.this);
        mProgressDialog.setMessage("Loading please Wait");*/
        // mProgressDialog.show();
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null, "NewsID DESC");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {

        if(cursor!=null) {
            cursor.moveToFirst();
        }
       /* mProgressDialog.dismiss();*/
        FeedsCustomAdapter mAdapter = new FeedsCustomAdapter(getActivity(), cursor, 0);
        mFeedsListView.setAdapter(mAdapter);
        mSwipeContainer.post(new Runnable() {
            @Override
            public void run() {
                mSwipeContainer.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRefresh() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.action.download");
        getActivity().sendBroadcast(intent);
        getActivity(). getSupportLoaderManager().restartLoader(CONTACT_LOADER_ID, null, this);
    }
}

