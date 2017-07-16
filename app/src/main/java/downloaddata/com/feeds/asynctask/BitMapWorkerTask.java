package downloaddata.com.feeds.asynctask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import downloaddata.com.feeds.R;

/**
 * Async Task To download Images from the Url
 */
public class BitMapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private static String sData;
    private LruCache<String, Bitmap> mMemoryCache;


    private final WeakReference<ImageView> mImageViewReference;


    public BitMapWorkerTask(Context context, ImageView imageView) {
        mImageViewReference = new WeakReference<>(imageView);
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int mCacheSize = 1024 * 1024 * memClass / 8;
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number of items.
                return bitmap.getByteCount();
            }

        };  //this.path=imageView.getTag().toString();
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        sData = params[0];
        Bitmap bitmap = downloadBitmap(params[0]);
        if (bitmap != null) {
            addBitmapToMemoryCache(params[0], bitmap);
        }
        return bitmap;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (mImageViewReference != null && bitmap != null) {
            final ImageView imageView = mImageViewReference.get();
            final BitMapWorkerTask bitmapWorkerTask =
                    getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            if (getBitmapFromMemCache(sData) != null)
                imageView.setImageBitmap(getBitmapFromMemCache(sData));
        }
    }

    /**
     * Adds the bitmap to the memory cache
     * @param key image key
     * @param bitmap image bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * Gets the Bitmap from the memory cache
     * @param key bitmap key
     * @return Bitmap from the memory cache
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return (Bitmap) mMemoryCache.get(key);
    }

    /**
     * Loads the bitmap
     * @param context app context
     * @param path path
     * @param imageView image view
     */
    public void loadBitmap(Context context, String path, ImageView imageView) {

        Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(),R.drawable.placeholder);
        if (cancelPotentialWork(path, imageView)) {
            BitMapWorkerTask bitMapWorkerTask = new BitMapWorkerTask(context, imageView);
            final AysncDrawable aysncDrawable = new AysncDrawable(context.getResources(), bitMapWorkerTask, placeholder);
            imageView.setImageDrawable(aysncDrawable);
            bitMapWorkerTask.execute(path);

        }
    }

    /**
     * Cancel worker task in progress
     * @param path path
     * @param imageView image vie
     * @return true /false
     */
    private static boolean cancelPotentialWork(String path, ImageView imageView) {
        final BitMapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = sData;
            if (bitmapData != path) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitMapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AysncDrawable) {
                final AysncDrawable asyncDrawable = (AysncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    private class AysncDrawable extends BitmapDrawable {
        private final WeakReference<BitMapWorkerTask> bitMapWorkerTaskReference;

        AysncDrawable(Resources r, BitMapWorkerTask bitMapWorkerTask, Bitmap bitmap) {
            super(r, bitmap);
            bitMapWorkerTaskReference = new WeakReference<BitMapWorkerTask>(bitMapWorkerTask);

        }

        BitMapWorkerTask getBitmapWorkerTask() {
            return bitMapWorkerTaskReference.get();
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            // urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}


