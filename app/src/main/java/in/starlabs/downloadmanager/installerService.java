package in.starlabs.downloadmanager;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Arvind on 08/01/16.
 */
public class installerService  {

    public void installApk(String filename) {
        Log.i("Action Log - DM - ", "installer service - installApk - url -");
           String path = "/sdcard/Download/"+filename;
            try {
                final String command = "cp -r " + path +" "+"/data/app/" ;
//                final String command = "pm install -r " + filename;

                Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
