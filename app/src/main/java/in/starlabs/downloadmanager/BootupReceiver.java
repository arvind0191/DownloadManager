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

/**
 * Created by Arvind on 07/01/16.
 */
public class BootupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Action Log - BootupReceiver -", "OnReieve");

       Utils.IMEI = getIMEI(context);
        Utils.WIFIMAC = getmacAddress(context);

        Fabric.with(context, new Crashlytics());
        new GetAppListTask(context).execute(Utils.ServerURL);

        Log.e("Action Log - BootupReceiver -", "OnReieve - got imei - "+Utils.IMEI +" wifi mac - "+Utils.WIFIMAC);

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
