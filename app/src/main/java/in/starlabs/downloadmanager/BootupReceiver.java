package in.starlabs.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;

/**
 * Created by Arvind on 07/01/16.
 */
public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Action Log - BootupReceiver -","OnReieve");
        Fabric.with(context, new Crashlytics());
        new GetAppListTask(context).execute(Utils.ServerURL);
    }
}
