package downloaddata.com.feeds.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Content Provider Class for Creating Tables
 */
public class RssFeedProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.download.service";
    public static final String URL = "content://" + PROVIDER_NAME + "/CricketDb";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static HashMap<String, String> sValues;
    public static final String TABLE_NAME = "CricketNews";


    /**
     * Database Primary Key Column
     */
    public static final String NEWS_ID = "_id";
    public static final String ITEM_ID = "NewsID";
    public static final String HEADLINE = "HeadLines";
    public static final String LINK = "LINK";
    public static final String CAPTION = "CAPTION";
    public static final String AUTHOR = "AUTHOR";
    public static final String DATE = "DATE";
    public static final String PHOTO = "PHOTO";
    public static final String THUMB = "THUMB";
    public static final String STORY = "Story";

    /**
     * Query to Create a Table
     */
    static final String CREATE_FEEDS_TABLE = "create table " + TABLE_NAME + "(_id INTEGER " +
            "PRIMARY KEY AUTOINCREMENT, " + " NewsID TEXT NOT NULL," + " HeadLines TEXT NOT NULL," + " LINK TEXT," + " " +
            "CAPTION TEXT," + "AUTHOR TEXT," + " DATE TEXT NOT NULL," + " PHOTO Text," + "THUMB Text," + " Story TEXT);";

    static final int uriCode = 1;
    static final String DATABASE_NAME = "CricketDb";
    static final int DATABASE_VERSION = 5;
    private SQLiteDatabase database;


    static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, DATABASE_NAME, uriCode);
    }

    /**
     * Helper Class for Performing SqlLite Operations
     */
    private static class CricketNewsFeedHelper extends SQLiteOpenHelper {


        public CricketNewsFeedHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FEEDS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        CricketNewsFeedHelper helper = new CricketNewsFeedHelper(context);
        database = helper.getWritableDatabase();
        return (database == null) ? false : true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case uriCode:

                // A projection map maps from passed column names to database column names
                queryBuilder.setProjectionMap(sValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Cursor provides read and write access to the database
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null,
                null, sortOrder);

        // Register to watch for URI changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = database.insert(TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
