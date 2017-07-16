package downloaddata.com.feeds.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import downloaddata.com.feeds.service.DownloadDataService;


/**
 * Receiver to Start the Download Service On Broadcast
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startDownloadService = new Intent(context, DownloadDataService.class);
        context.startService(startDownloadService);
    }
}
