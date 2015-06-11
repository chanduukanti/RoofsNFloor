package com.example.mapslist;

//import android.app.Activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MarkerOptionsCreator;
import com.google.android.maps.MapActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;

public class MapView extends Activity  implements OnMapClickListener,
OnMapLongClickListener, OnMarkerClickListener, OnCameraChangeListener{

    final String BUNDLE_KEY_LOCATION = "location";
    final String BUNDLE_KEY_LATITUDE = "latitude";
    final String BUNDLE_KEY_LONGITUDE = "longitude";
    final String BUNDLE_KEY_SNIPPET = "snippet";

    AlertDialog mLocationDisabledMsgDialog;
    SharedPreferences sharedPreferences;
    final int DEFAULT_MAP_ZOOM_LEVEL = 15;      
    ImageButton mCurrentLocation;

    LatLng mCurrentLatLng;
    ArrayList<Project> pList;
    Marker mCurrentMarker;
    String url = "http://54.254.240.217:8080/app-task/projects/";
    GoogleMap mGoogleMap;
    SharedPreferences mSharePreference;
    LocationManager mLocationManager;
    boolean mIsGpsOn;
    boolean mIsNetworKOn;
    private boolean isCreateFail = false;
    private float mMapZoomLevel = DEFAULT_MAP_ZOOM_LEVEL;
    // private LocationListener mLocationListner;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        isCreateFail = !checkGooglePlayServices();
        setContentView(R.layout.map_view);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Projects");
        if (!isCreateFail) {
            init();
        }
        if (mGoogleMap != null) {
            new DownloadJsonInternet().execute(url);
        }
        /*        if (mGoogleMap != null) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, mMapZoomLevel);
            if (cu != null)
                mGoogleMap.moveCamera(cu);
        }*/
    }

    class DownloadJsonInternet extends AsyncTask<String, String, ArrayList<Project>> {

        protected  ArrayList<Project> doInBackground(String...params) {
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
            return doParseJson(builder.toString());
        }

        @Override
        protected void onPostExecute(ArrayList<Project> projList) {
            for (int i = 0; i < projList.size(); i++) {                
                setLatLngAndMarker( projList.get(i));
                // Moving CameraPosition to last clicked position
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(projList.get(i).latitude, projList.get(i).longitude)));
                // Setting the zoom level in the map on last position  is clicked
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(mMapZoomLevel));
            }


        }
    }
    private void init() {

        mMapZoomLevel = 15;
        if (!setUpView()) {
            return;
        }

        // Check the GPS/Network option value in the Settings
        // application
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(
                getContentResolver(), LocationManager.GPS_PROVIDER);
        boolean networkEnabled = Settings.Secure.isLocationProviderEnabled(
                getContentResolver(), LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            if (mLocationDisabledMsgDialog == null) {
                showDialog();
            } else {
                mLocationDisabledMsgDialog.show();
            }
        } else {
            //   requestLatLngAndMarker(true);

            sharedPreferences = getSharedPreferences("projects", 0);
        }

        /*        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location =     
                mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
        if (location != null)
            mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        else 
            mCurrentLatLng = new LatLng(90.0, 0.0);

        if (checkSetting()) {
            requestLatLngAndMarker(true);
        }*/
    }

    public ArrayList<Project> doParseJson(String string) {
        // TODO Auto-generated method stub
        try{
            //JSONObject jobj = new JSONObject(file_url);
            JSONArray jarr = new JSONArray(string);
            pList = new ArrayList<Project>();
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject c = jarr.getJSONObject(i);
                String id = c.getString("id");
                String pname = c.getString("projectName");
                Double lat = c.getDouble("lat");
                Double longg = c.getDouble("lon");
                //list.add(pname);
                pList.add(new Project(id,pname,lat,longg)); 
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.v("chandu", "JSON Exception error occured");
            e.printStackTrace();
        }
        return pList;
    }

    protected void onDestroy() {
        /*        if ( mLocationManager != null ){
            mLocationManager.removeUpdates(mLocationListner);
            mLocationManager = null;
        }*/

        if (mGoogleMap != null) {
            mGoogleMap.setOnMapClickListener(null);
            mGoogleMap.setOnMapLongClickListener(null);
            mGoogleMap.setOnMarkerClickListener(null);
            mGoogleMap.setOnCameraChangeListener(null);
        }
        if (mGoogleMap != null){
            mGoogleMap.clear();
            mGoogleMap = null;
        }
        System.gc();
        super.onDestroy();
    }
    private boolean checkSetting() {

        boolean isShowDialog = false;

        StringBuilder dialogString = new StringBuilder();

        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        mIsGpsOn = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        mIsNetworKOn = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        dialogString.append("string_not_all_location_sources_are_currently_enabled_desc");

        if (!(mIsGpsOn && mIsNetworKOn)) {
            dialogString.append("\n\n");
            dialogString
            .append("string_turn_on_gps_and_enable_wireless_networks_in_location_settings");
            isShowDialog = true;
        }
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            // Toast.makeText(mContext, "string_data_connection_failed", Toast.LENGTH_SHORT).show();
            dialogString.append("\n\n");
            dialogString.append("string_turn_on_wifi");
            isShowDialog = true;
        }

        if (isShowDialog ) {
            // showCheckSettingDialog(dialogString.toString());
            return false;
        }
        return true;
    }


    private void showDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(MapView.this);
        ab.setTitle("GPS diabled");
        ab.setMessage("GPS diabled");
        ab.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        ab.setNegativeButton("CANCEL", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        if (!this.isFinishing()) {
            mLocationDisabledMsgDialog = ab.show();
        }
    }
    private boolean setUpView() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(
                R.id.mapview_select);
        if (mapFragment == null) {
            finish();
            return false;
        }
        mGoogleMap = mapFragment.getMap();

        if (mGoogleMap == null) {
            finish();
            return false;
        }
        System.gc();
        mGoogleMap.clear();

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);

        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnCameraChangeListener(this);

        return true;
    }
    @Override
    protected void onResume() {
        if (isCreateFail) {
            isCreateFail = !checkGooglePlayServices();
            if (isCreateFail) {
                finish();
            } 
        }
        super.onResume();
        //       mSearchText.clearFocus();
    }
    private boolean checkGooglePlayServices() {
        boolean isGooglePlayServicesAvailable = false;
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        switch (errorCode) {
        case ConnectionResult.SUCCESS:
            isGooglePlayServicesAvailable = true;
            break;
        case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            isGooglePlayServicesAvailable = false;
            break;
        }

        return isGooglePlayServicesAvailable;
    }

    /*private final LocationListener mLocationListner = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onProviderDisabled(String arg0) {

        }

        @Override
        public void onLocationChanged(Location location) {
            if (mGoogleMap != null)
                setLatLngAndMarker(true, new LatLng(location.getLatitude(), location.getLongitude()));
            if (mLocationManager != null)
                mLocationManager.removeUpdates(mLocationListner);
        }
    };
     */

    /*   private void requestLatLngAndMarker(Boolean needSetText) {
        List<String> mProvider = mLocationManager.getAllProviders();
        if (mProvider == null) {
            return;
        }

        Location location = null;
        for (String provider : mProvider) {
            location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                break;
            }
        }

        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)
                && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, mLocationListner);

        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)
                && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, mLocationListner);

    }*/
    @Override
    public void onCameraChange(CameraPosition pos) {
        // TODO Auto-generated method stub
        mCurrentLatLng = null;
        mCurrentLatLng = new LatLng(pos.target.latitude, pos.target.longitude);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        LatLng latlong =  marker.getPosition();
        Project proj = MainActivity.myProjectsMap.get(latlong);
        Intent in = new Intent("roomandfloors.view.projectDetails");
        in.putExtra("Idurl", url+proj.id);
        in.putExtra("ProjectName", proj.projectName);
        startActivity(in);
        finish();
        return true;
    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }
    private void setLatLngAndMarker(Project p) {
        mCurrentLatLng = new LatLng(p.latitude, p.longitude);
        if (mGoogleMap != null) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, mMapZoomLevel );
            if (cu != null)
                mGoogleMap.moveCamera(cu);
        } else {
            return;
        }
        MarkerOptions mo = new MarkerOptions().position(mCurrentLatLng)
                .title(p.projectName);


        if (mo != null && mGoogleMap != null)
            mCurrentMarker = mGoogleMap.addMarker(mo);

        if (mCurrentMarker != null) {
            mCurrentMarker.setPosition(mCurrentLatLng);
        }

    }

}

