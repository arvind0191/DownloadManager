package in.starlabs.downloadmanager;

import android.content.Context;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        Log.i("Action Log - DownloadManager - ", "GetAppListTask - DoinBackground - url -"+tesURL);
        return getList(tesURL);
    }

    private String getList(String tesURL) {

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(tesURL);
            /*
             HttpClient is more then less deprecated. Need to change to URLConnection
              */

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("IMEI", Utils.IMEI);
            params.put("WIFIMAC", Utils.WIFIMAC);


            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Content-length", String.valueOf(postDataBytes.length));
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.getOutputStream().write(postDataBytes);
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
        Log.i("Action Log - DownloadManager - ", "GetAppListTask - onPostExecute - applist"+appList);
        List<String> DownloadedList = getDownloadedAppList();
        for (String getFile: appList.split("&")){
            if(!DownloadedList.contains(getFile)) {
                Log.i("Action Log - DownloadManager -", "filename  is: Utils.ServerURL+getFile" + getFile);
                new DownloaderTask(context).execute(Utils.DownloadUrl + getFile,getFile);
            }
        }
        super.onPostExecute(appList);
    }

    private List<String> getDownloadedAppList() {
        List<String> list = new ArrayList<>();

        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".mp3");
                }
            });
            for (File each: files) {
                list.add(each.toString());
            }
            Log.i("Action Log - DownloadManager - "," file list - "+list.toString());
        } else {

        }
        return list;
    }
}
