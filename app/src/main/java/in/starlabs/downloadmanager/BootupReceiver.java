package in.starlabs.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

/**
 * Created by Arvind on 07/01/16.
 */
public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Action Log - DownloadManager - BootupReceiver -", "OnReieve");

        Utils.IMEI = getIMEI(context);
        Utils.WIFIMAC = getmacAddress(context);

        Fabric.with(context, new Crashlytics());
        // TODO: Use your own attributes to track content views in your app
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("BootupReciver")
                .putContentType("Reciever"));

        new GetAppListTask(context).execute(Utils.ServerURL);

        Log.i("Action Log - DownloadManager - BootupReceiver -", "OnReieve - got imei - " + Utils.IMEI + " wifi mac - " + Utils.WIFIMAC);

    }

    private String getmacAddress(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    private String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
