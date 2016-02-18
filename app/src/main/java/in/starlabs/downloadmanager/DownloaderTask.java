package in.starlabs.downloadmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arvind on 08/01/16.
 */
public class DownloaderTask extends AsyncTask <String, Integer, String> {

    private Context context;

    private PowerManager.WakeLock mWakeLock;

    public DownloaderTask(Context context) {
        this.context = context;
//        this.filename = filename;

    }

    @Override
    protected String doInBackground(String... sUrl) {
        String filename = "";
        filename = sUrl[1];
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        java.io.File file = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/"+filename);

        //create the file
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            URL url = new URL(sUrl[0]);
            Log.i("Action Log - DownloadManager - ", "GetAppListTask - DoinBackground - url -" +url );

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
            return filename;
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String apkName) {
        Log.i("Action Log - DownloadManager - Response get in postexecute is - ", "" + apkName);
        installerService service = new installerService();
        service.installApk(apkName);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/GestureBuilder.apk")), "application/vnd.android.package-archive");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
//        context.startActivity(intent);
        Log.i("Action Log - DownloadManager - Response get in postexecute is - done", "");
        super.onPostExecute(apkName);
    }
}