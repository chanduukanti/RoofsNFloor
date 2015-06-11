package com.example.mapslist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mapslist.MainActivity.DownloadJsonInternet;
import com.example.mapslist.MainActivity.MySimpleArrayAdapter;
import com.google.android.gms.maps.model.LatLng;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewFragment extends Fragment {
    Context mContext = null;
    AsyncTask<String, Integer, String> jsonTask;
    ListView lv;
    InputStream is = null;
    String json;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<Project> pList;
    List<String> locationList = new ArrayList<String>();
    MySimpleArrayAdapter adapter;
    public static String url = "http://54.254.240.217:8080/app-task/projects/";
    JSONObject jsonObject;
    public static HashMap<LatLng, Project> myProjectsMap = new HashMap<LatLng, Project>();
    private DownloadJsonInternet mDownloadJsonInternet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.listview_fragment, container,
                false);
        lv = (ListView) rootView.findViewById(R.id.projectList);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mDownloadJsonInternet = new DownloadJsonInternet();
        mDownloadJsonInternet.execute(url);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        ActionBar ab = getActivity().getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Projects");

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long arg3) {
                // TODO Auto-generated method stub
                Project p = (Project) adapter.getItemAtPosition(position);
                Intent in = new Intent("roomandfloors.view.projectDetails");
                in.putExtra("Idurl", url + p.id);
                in.putExtra("ProjectName", p.projectName);
                startActivity(in);
            }
        });
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        lv = null;
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (null != mDownloadJsonInternet)
            mDownloadJsonInternet.cancel(true);
        mDownloadJsonInternet = null;
    }

    // getting location address by LatLang
    private String updateLocation(LatLng point) {
        String location = "";
        List<Address> data = null;

        try {
            Geocoder geoCoder = new Geocoder(mContext, Locale.getDefault());
            data = geoCoder.getFromLocation(point.latitude, point.longitude, 1);

            if (data.size() > 0) {
                Address address = data.get(0);

                int addressLines = address.getMaxAddressLineIndex();
                for (int i = 0; i <= 1; i++) {
                    String addressLine = address.getAddressLine(i);
                    if (!TextUtils.isEmpty(addressLine)) {
                        if (i == 0) {
                            location = addressLine;
                        } else {
                            location += " " + addressLine;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    class DownloadJsonInternet extends
            AsyncTask<String, String, ArrayList<Project>> {

        protected ArrayList<Project> doInBackground(String... params) {
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
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
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
            // return builder.toString();
            return doParseJson(builder.toString());
        }

        @Override
        protected void onPostExecute(ArrayList<Project> file_url) {
            // pList = doParseJson(file_url);

            adapter = new MySimpleArrayAdapter(mContext, file_url);
            if (lv != null)
                lv.setAdapter(adapter);
        }
    }

    public ArrayList<Project> doParseJson(String json) {
        // TODO Auto-generated method stub
        Log.i("chandu", "doing parsing Json array" + json);
        try {
            // JSONObject jobj = new JSONObject(file_url);
            JSONArray jarr = new JSONArray(json);
            pList = new ArrayList<Project>();
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject c = jarr.getJSONObject(i);
                String id = c.getString("id");
                String pname = c.getString("projectName");
                Double lat = c.getDouble("lat");
                Double longg = c.getDouble("lon");
                // list.add(pname);
                pList.add(new Project(id, pname, lat, longg));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.v("chandu", "JSON Exception error occured");
            e.printStackTrace();
        }
        // makeMyprojects(pList);
        return pList;
    }

    class MySimpleArrayAdapter extends ArrayAdapter<Project> {
        private final Context context;
        private final List<Project> values;

        public MySimpleArrayAdapter(Context context, List<Project> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.project_list_item, parent,
                    false);
            TextView pname = (TextView) rowView.findViewById(R.id.projectName);
            pname.setText(values.get(position).projectName);

            return rowView;
        }
    }

    private void makeMyprojects(ArrayList<Project> projectList) {
        // TODO Auto-generated method stub
        for (int i = 0; i < projectList.size(); i++) {
            LatLng latlang = new LatLng(projectList.get(i).latitude,
                    projectList.get(i).longitude);
            locationList.add(updateLocation(latlang));
            myProjectsMap.put(latlang, projectList.get(i));
        }
    }
}