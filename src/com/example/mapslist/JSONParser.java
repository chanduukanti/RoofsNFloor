package com.example.mapslist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;
 
public class JSONParser {
    
    static String json = "";
 
    InputStream inputStream = null;
    BufferedReader reader = null;
 
    public JSONParser() {
    }
    // using HttpClient for <= Froyo
    public String getJSONHttpClient(String url)
            throws ClientProtocolException, IOException, JSONException {
        //JSONObject jsonObject = null;
    	new DownloadJsonInternet().execute(url);
    	try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
    }
	class DownloadJsonInternet extends AsyncTask<String, String, String> {

		protected  String doInBackground(String...params) {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} else {
					Log.e("chandu", "Failed to download file");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return builder.toString();
		}

		@Override
		protected void onPostExecute(String file_url) {
			//pList = doParseJson(file_url);
			Log.v("chandu", "Json string is : "+file_url);
			json = file_url;
		}
	}
}
