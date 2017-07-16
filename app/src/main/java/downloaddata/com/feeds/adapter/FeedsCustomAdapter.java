package downloaddata.com.feeds.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import downloaddata.com.feeds.R;
import downloaddata.com.feeds.asynctask.BitMapWorkerTask;

/**
 * Custom cursor Adapter to Bind View nd Display Multiple Layouts Depending Upon the data Type
 */
public class FeedsCustomAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private BitMapWorkerTask mBitMapWorkerTask;

    public FeedsCustomAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mLayoutInflater = LayoutInflater.from(context);


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (cursor.getString(cursor.getColumnIndex("PHOTO")) == null) {
            View view = mLayoutInflater.inflate(R.layout.news_list_item, parent, false);
            holder.HeadLinetv = (TextView) view.findViewById(R.id.HeadLine_textView);
            holder.Keywordstv = (TextView) view.findViewById(R.id.Keywords_textView);
            holder.Datetv = (TextView) view.findViewById(R.id.Date_textView);
            holder.LinkTv = (TextView) view.findViewById(R.id.Link_textView);
            view.setTag(holder);
            return view;
        } else {
            View view = mLayoutInflater.inflate(R.layout.news_list_item_image, parent, false);
            holder.HeadLinetv = (TextView) view.findViewById(R.id.HeadLine_textView);
            holder.Keywordstv = (TextView) view.findViewById(R.id.Keywords_textView);
            holder.Datetv = (TextView) view.findViewById(R.id.Date_textView);
            holder.PhotoIv = (ImageView) view.findViewById(R.id.Photo_ImageView);
            holder.LinkTv = (TextView) view.findViewById(R.id.Link_textView);
            view.setTag(holder);
            return view;
        }
    }


    private int getItemViewType(Cursor cursor) {
        Log.d("Cursor", "" + cursor.getCount());
        String Photo = cursor.getString(cursor.getColumnIndex("PHOTO"));
        if (Photo != null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return getItemViewType(cursor);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        int type = getItemViewType(cursor);
        holder.HeadLinetv.setText(cursor.getString(cursor.getColumnIndex("HeadLines")));
        holder.Keywordstv.setText(cursor.getString(cursor.getColumnIndex("CAPTION")));
        holder.Datetv.setText(cursor.getString(cursor.getColumnIndex("DATE")));
        holder.LinkTv.setText(cursor.getString(cursor.getColumnIndex("LINK")));
        mBitMapWorkerTask= new BitMapWorkerTask(context,holder.PhotoIv);
        if (type == 0) {
            final Bitmap bm = mBitMapWorkerTask.getBitmapFromMemCache(cursor.getString(cursor.getColumnIndex("PHOTO")));
            if (bm == null) {
                mBitMapWorkerTask.loadBitmap(context, cursor.getString(cursor.getColumnIndex("PHOTO")), holder.PhotoIv);
            }
            else
            {
                holder.PhotoIv.setImageBitmap(bm);
            }
        }

    }

    private static class ViewHolder {
        TextView HeadLinetv;
        TextView Keywordstv;
        TextView Datetv;
        TextView LinkTv;
        ImageView PhotoIv;
    }



}
