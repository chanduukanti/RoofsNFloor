package com.roofnfloors.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.roofnfloors.cache.FileCache;
import com.roofnfloors.tasks.ProjectDataFetcherTask.ProjectDataFetcherCallBack;
import com.roofnfloors.ui.ProjectDetailsActivity;
import com.roofnfloors.util.Utility;

public class ServerDataFetcher {
    private static final String TAG = ServerDataFetcher.class
            .getCanonicalName();

    private ServerDataFetcher() {

    }

    public static String getServerResult(String url) {
        Log.d("Test", TAG + "getServerResult");
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        InputStream content = null;
        HttpEntity entity = null;
        try {
            Log.d("Test", TAG + "getServerResult-Sent request");
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                Log.d("Test", TAG + "getServerResult-Response received");
                entity = response.getEntity();
                content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(TAG, "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (content != null) {
                try {
                    content.close();
                    content = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Test", TAG + "getServerResult-Releasing HTTP get resources");
        }
        return builder.toString();
    }

    public static Bitmap downloadImage(String url, Context context) {
        File f = FileCache.getFileCache(context).getFile(url);

        // from SD cache
        Bitmap b = Utility.decodeFile(f);
        if (b != null)
            return b;
        InputStream in = null;
        Bitmap bmap = null;
        OutputStream os = null;
        int response = -1;
        URL urlString;
        URLConnection conn = null;
        HttpURLConnection httpConn = null;
        try {
            urlString = new URL(url);
            conn = urlString.openConnection();
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");
            httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
            bmap = BitmapFactory.decodeStream(in);
            os = new FileOutputStream(f);
            Utility.CopyStream(in, os);
            return Utility.decodeFile(f);
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                    os = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            if (httpConn != null) {
                httpConn.disconnect();
                httpConn = null;
            }
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bmap;
    }

    public static final void downloadFirstImage(String url, int length,
            ProjectDataFetcherCallBack mCallBack) {
        Log.d(TAG, "downloadFirstImageAndSet");
        Bitmap map = downloadImage(url, (ProjectDetailsActivity) mCallBack);
        ((ProjectDetailsActivity) mCallBack).onFirstImageDownloaded(map,length);
    }
}