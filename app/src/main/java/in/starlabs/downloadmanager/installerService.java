package in.starlabs.downloadmanager;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Arvind on 08/01/16.
 */
public class installerService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public installerService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
