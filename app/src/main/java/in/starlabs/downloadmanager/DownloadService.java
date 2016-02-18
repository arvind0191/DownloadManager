package in.starlabs.downloadmanager;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Arvind on 18/02/16.
 */
public class DownloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
