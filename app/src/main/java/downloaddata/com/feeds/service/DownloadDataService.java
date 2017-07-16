package downloaddata.com.feeds.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import downloaddata.com.feeds.contentprovider.RssFeedProvider;
import downloaddata.com.feeds.model.NewsItem;

/**
 *
 * Service to Download and parse json data from the server
 */
public class DownloadDataService extends Service {

    InputStream mInputStream = null;

    HttpURLConnection mUrlConnection = null;

    Integer mResult = 0;

    ContentResolver mResolver;

    ArrayList<NewsItem> mNewsItems = new ArrayList<>();


    /**
     * Method to add New Feeds to the Database
     *
     * @param itemsList List of Item to Be added to database on Each Request to the server
     */
    public void addFeeds(ArrayList<NewsItem> itemsList) {

        Cursor cursor = mResolver.query(RssFeedProvider.CONTENT_URI, null, null, null, "NewsID DESC");
        //To check whether the item is already placed in the Database
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int maxid = Integer.parseInt(cursor.getString(1));
                Log.d("Compare" + maxid, "");
                for (NewsItem item : itemsList) {
                    int newid = Integer.parseInt(item.getmItemId());
                    if (maxid < newid) {
                        ContentValues values = new ContentValues();
                        values.put(RssFeedProvider.ITEM_ID, item.getmItemId());
                        values.put(RssFeedProvider.HEADLINE, item.getmHeadline());
                        values.put(RssFeedProvider.AUTHOR, item.getmAuthor());
                        values.put(RssFeedProvider.DATE, item.getmDate());
                        values.put(RssFeedProvider.CAPTION, item.getmCaption());
                        values.put(RssFeedProvider.LINK, item.getmLink());
                        values.put(RssFeedProvider.PHOTO, item.getmPhoto());
                        values.put(RssFeedProvider.THUMB, item.getmThumbImage());
                        values.put(RssFeedProvider.STORY, item.getmStory());
                        getContentResolver().insert(RssFeedProvider.CONTENT_URI, values);
                    }
                    Log.d("Compare" + maxid, "" + newid);
                }
                itemsList.clear();
            } else {
                for (NewsItem item : itemsList) {
                    ContentValues values = new ContentValues();
                    values.put(RssFeedProvider.ITEM_ID, item.getmItemId());
                    values.put(RssFeedProvider.HEADLINE, item.getmHeadline());
                    values.put(RssFeedProvider.AUTHOR, item.getmAuthor());
                    values.put(RssFeedProvider.DATE, item.getmDate());
                    values.put(RssFeedProvider.CAPTION, item.getmCaption());
                    values.put(RssFeedProvider.LINK, item.getmLink());
                    values.put(RssFeedProvider.PHOTO, item.getmPhoto());
                    values.put(RssFeedProvider.THUMB, item.getmThumbImage());
                    values.put(RssFeedProvider.STORY, item.getmStory());
                    getContentResolver().insert(RssFeedProvider.CONTENT_URI, values);

                }
                itemsList.clear();
            }
        }
    }
    /**
     * Asynch Task to Download Data in the Background
     */
    private class DownloadFilesTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("Praveen", mNewsItems.toString());
            addFeeds(mNewsItems);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL("http://mfeeds.timesofindia.indiatimes.com/Feeds/jsonfeed?newsid=4719161&format=simplejson");
                mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                mUrlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                mUrlConnection.setRequestMethod("GET");
                int statusCode = mUrlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    mInputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                    String response = convertInputStreamToString(mInputStream);
                    mNewsItems = parseResult(response);
                    mResult = 1; // Successful
                } else {
                    mResult = 0; //"Failed to fetch data!";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        super.onCreate();

    }

    /**
     * Method to Convert Input Stream to String
     *
     * @param inputStream
     * @return String to parse
     * @throws IOException
     */
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mResolver = getContentResolver();
        new DownloadFilesTask().execute();
        return START_STICKY;
    }


    /**
     * Metod to Parse Json Data
     *
     * @param result List of News Item obtained in each request to the server
     * @return
     */
    private ArrayList<NewsItem> parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("NewsItem");
            for (int i = 0; i < posts.length(); i++) {
                JSONObject news = posts.optJSONObject(i);

                NewsItem item = new NewsItem();

                item.setmHeadline(news.getString("HeadLine"));
                if (news.opt("ByLine") != null) {
                    item.setmAuthor(news.getString("ByLine"));
                }
                item.setmDate(news.getString("DateLine"));
                item.setmLink(news.getString("WebURL"));
                if (null != news.opt("Caption")) {
                    item.setmCaption(news.getString("Caption"));
                }
                if (null != news.opt("Photo")) {
                    item.setmPhoto(news.getString("Photo"));
                }
                if (null != news.opt("Thumb")) {
                    item.setmThumbImage(news.getString("Thumb"));
                }
                item.setmItemId(news.getString("NewsItemId"));
                item.setmStory(news.getString("Story"));
                mNewsItems.add(item);
                Log.d("Praveen", item.toString());

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mNewsItems;
    }


}
