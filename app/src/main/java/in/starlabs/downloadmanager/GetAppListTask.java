package in.starlabs.downloadmanager;

import android.content.Context;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Arvind on 07/01/16.
 */
public class GetAppListTask extends AsyncTask<String, String, String> {
    Context context; // give the context to the task by calling with constructor
   public GetAppListTask(Context context){
       this.context = context;
   }


//    <?php
//            $path = realpath('your path\\your folder');
//    foreach (new RecursiveIteratorIterator(new RecursiveDirectoryIterator($path)) as $filename)
//    {
//        if (strpos($filename,'.mp3') !== false)
//            $filenm=$filenm ."-". basename($filename);
//    }
//    echo $filenm
//    ?>

    @Override
    protected String doInBackground(String... params) {

        String tesURL = params[0];
        java.io.File file = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/Filename.xml");

        //create the file
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("Action Log - DM - ", "GetAppListTask - DoinBackground - url -"+tesURL);
        return getList(tesURL);
    }

    private String getList(String tesURL) {

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(tesURL);
            /*
             HttpClient is more then less deprecated. Need to change to URLConnection
              */
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.addRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();
            switch (statusCode) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (IllegalStateException e3) {
            Log.e("IllegalStateException", e3.toString());
            e3.printStackTrace();
        } catch (IOException e4) {
            Log.e("IOException", e4.toString());
            e4.printStackTrace();
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String appList) {
        for (String getFile: appList.split("&")){
            Log.d("abc","filename  is: Utils.ServerURL+getFile" + getFile);
            new DownloaderTask(context).execute(Utils.DownloadUrl+getFile);
        }
        super.onPostExecute(appList);
    }
}
