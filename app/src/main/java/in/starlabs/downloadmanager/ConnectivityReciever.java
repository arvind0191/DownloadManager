package in.starlabs.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Arvind on 07/01/16.
 */
public class ConnectivityReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            new GetAppListTask(context).execute(Utils.ServerURL);
            Log.i("Action Log - ConnectivityReciever -", "Connectivity changed - " + isConnected);
        } else {
            Log.i("Action Log - ConnectivityReciever -", "Connectivity changed - " + isConnected);
        }

    }
}
