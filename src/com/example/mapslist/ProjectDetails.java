package com.example.mapslist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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

import com.example.mapslist.ProjectInfo.Documents;
import com.example.mapslist.ProjectInfo.Summary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ProjectDetails extends Activity implements OnClickListener {

    ViewPager viewPager;
    PagerAdapter adapter;
    // Drawable[] images;
    String detailUrl;
    String projectName;
    ActionBar actionbar;
    public String json;
    Drawable[] imageArray;
    Drawable[] defaultArray;
    String[] imageLinks;
    JSONObject jsonObj;
    private GetProjectInfoTask mProjectInfoTask;
    private DownloadImageTask mDownloadImageTask;
    private static final String TAG = "ProjectDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_item_details);
        Intent detailIntent = getIntent();
        detailUrl = detailIntent.getStringExtra("Idurl");
        projectName = detailIntent.getStringExtra("ProjectName");
        actionbar = getActionBar();
        actionbar.setTitle(projectName);
        actionbar.setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.image_pager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onPageSelected:" + position);
                boolean isImageToDownload = (imageArray[position] == null);
                if (isImageToDownload) {
                    Log.d(TAG, "isImageToDownload:" + isImageToDownload);
                    // String links[] = new String[1];
                    downLoadImage(imageLinks[position], position);
                } else {
                    Log.d(TAG, "isImageToDownload:" + isImageToDownload);
                    ((ImagePagerAdapter) viewPager.getAdapter())
                            .getHolderViews()[position].getView()
                            .setBackground(imageArray[position]);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        mProjectInfoTask = new GetProjectInfoTask();
        mProjectInfoTask.execute(detailUrl);
        defaultArray = new Drawable[1];
        defaultArray[0] = getResources().getDrawable(R.drawable.ic_launcher);
        adapter = new ImagePagerAdapter(ProjectDetails.this, defaultArray);
        if (viewPager != null)
            viewPager.setAdapter(adapter);
        // Locate the ViewPager in viewpager_main.xml

        // Pass results to ViewPagerAdapter Class
        /*
         * adapter = new ImagePagerAdapter(ProjectDetails.this,images); // Binds
         * the Adapter to the ViewPager viewPager.setAdapter(adapter);
         */
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mProjectInfoTask)
            mProjectInfoTask.cancel(Boolean.TRUE);
        if (null != mDownloadImageTask) {
            mDownloadImageTask.cancel(Boolean.TRUE);
        }
        mProjectInfoTask = null;
        viewPager = null;
        adapter = null;
        imageArray = null;
        defaultArray = null;
        imageLinks = null;
        jsonObj = null;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

    private void downLoadImage(String URL, int pos) {
        if (null != mDownloadImageTask)
            mDownloadImageTask.cancel(Boolean.TRUE);
        mDownloadImageTask = new DownloadImageTask(pos);
        mDownloadImageTask.execute(URL);
    }

    class GetProjectInfoTask extends AsyncTask<String, Drawable[], ProjectInfo> {

        protected ProjectInfo doInBackground(String... params) {
            Log.d(TAG, "GetProjectInfoTask");
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
            ProjectInfo projectInfo = doJsonParse(builder.toString());
            if (projectInfo == null && projectInfo.imageUrls != null
                    && projectInfo.imageUrls.length <= 0) {
                return null;
            }
            imageLinks = new String[projectInfo.imageUrls.length];
            for (int i = 0; i < projectInfo.imageUrls.length; i++) {
                imageLinks[i] = projectInfo.imageUrls[i].reference;
            }
            // publishProgress(projectInfo);
            // imageArray = downLoadImages(imageLinks,imageLinks.length);
            return projectInfo;
        }

        @Override
        protected void onProgressUpdate(Drawable[]... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            if (values[0] != null) {
                Log.d(TAG, "onProgressUpdate" + values[0]);
                adapter = new ImagePagerAdapter(ProjectDetails.this, values[0]);
                if (viewPager != null)
                    viewPager.setAdapter(adapter);
            }
        }

        @Override
        protected void onPostExecute(ProjectInfo info) {
            Log.d(TAG, "onPostExecute" + info);
            if (imageArray != null) {
                adapter = new ImagePagerAdapter(ProjectDetails.this, imageArray);
                if (viewPager != null)
                    viewPager.setAdapter(adapter);
            }
            // pList = doParseJson(file_url);
            // set text for all the views
            // get Map and show for location map
        }

        private ProjectInfo doJsonParse(String string) {
            Log.d(TAG, "doJsonParse");
            ProjectInfo proj = new ProjectInfo();
            try {
                JSONObject projObj = new JSONObject(string);
                if (projObj.has("documents")) {
                    JSONArray documents = projObj.getJSONArray("documents");
                    if (null != documents) {
                        ProjectInfo.Documents docs[] = new ProjectInfo.Documents[documents
                                .length()];
                        downloadFirstImageAndSet(documents.getJSONObject(0),
                                docs.length);
                        proj.imageUrls = docs;
                        for (int i = 0; i < documents.length(); i++) {
                            JSONObject docu = documents.getJSONObject(i);
                            docs[i] = proj.new Documents();
                            if (docu.has("directionFacing")) {
                                String add = docu.getString("directionFacing");
                                if (null != add && !add.equals("")) {
                                    docs[i].directionFacing = add;
                                }
                            }
                            if (docu.has("primary")) {
                                docs[i].primary = docu.getBoolean("primary");
                            }
                            if (docu.has("type")) {
                                String add = docu.getString("type");
                                if (null != add && !add.equals("")) {
                                    docs[i].type = add;
                                }
                            }
                            if (docu.has("reference")) {
                                String add = docu.getString("reference");
                                if (null != add && !add.equals("")) {
                                    docs[i].reference = add;
                                }
                            }
                            if (docu.has("text")) {
                                String add = docu.getString("text");
                                if (null != add && !add.equals("")) {
                                    docs[i].text = add;
                                }
                            }
                        }
                    }
                    if (projObj.has("addressLine1")) {
                        String add = projObj.getString("addressLine1");
                        if (null != add && !add.equals("")) {
                            proj.addressLine1 = add;
                        }
                    }
                    if (projObj.has("addressLine2")) {
                        String add = projObj.getString("addressLine2");
                        if (null != add && !add.equals("")) {
                            proj.addressLine2 = add;
                        }
                    }
                    if (projObj.has("brochure")) {
                        String add = projObj.getString("brochure");
                        if (null != add && !add.equals("")) {
                            proj.brochure = add;
                        }
                    }

                    if (projObj.has("city")) {
                        String add = projObj.getString("city");
                        if (null != add && !add.equals("")) {
                            proj.city = add;
                        }
                    }
                    if (projObj.has("description")) {
                        String add = projObj.getString("description");
                        if (null != add && !add.equals("")) {
                            proj.description = add;
                        }
                    }

                }

                if (projObj.has("hidePrice")) {
                    proj.hidePrice = projObj.getBoolean("hidePrice");
                }
                if (projObj.has("landmark")) {
                    String add = projObj.getString("landmark");
                    if (null != add && !add.equals("")) {
                        proj.landmark = add;
                    }
                }
                if (projObj.has("listingId")) {
                    String add = projObj.getString("listingId");
                    if (null != add && !add.equals("")) {
                        proj.listingId = add;
                    }
                }
                if (projObj.has("listingName")) {
                    String add = projObj.getString("listingName");
                    if (null != add && !add.equals("")) {
                        proj.listingName = add;
                    }
                }
                if (projObj.has("locality")) {
                    String add = projObj.getString("locality");
                    if (null != add && !add.equals("")) {
                        proj.locality = add;
                    }
                }
                if (projObj.has("maxArea")) {
                    String add = projObj.getString("maxArea");
                    if (null != add && !add.equals("")) {
                        proj.maxArea = add;
                    }
                }
                if (projObj.has("maxPrice")) {
                    String add = projObj.getString("maxPrice");
                    if (null != add && !add.equals("")) {
                        proj.maxPrice = add;
                    }
                }
                if (projObj.has("maxPricePerSqft")) {
                    String add = projObj.getString("maxPricePerSqft");
                    if (null != add && !add.equals("")) {
                        proj.maxPricePerSqft = add;
                    }
                }
                if (projObj.has("minArea")) {
                    String add = projObj.getString("minArea");
                    if (null != add && !add.equals("")) {
                        proj.minArea = add;
                    }
                }
                if (projObj.has("minPrice")) {
                    String add = projObj.getString("minPrice");
                    if (null != add && !add.equals("")) {
                        proj.minPrice = add;
                    }
                }
                if (projObj.has("minPricePerSqft")) {
                    String add = projObj.getString("minPricePerSqft");
                    if (null != add && !add.equals("")) {
                        proj.minPricePerSqft = add;
                    }
                }
                if (projObj.has("noOfAvailableUnits")) {
                    String add = projObj.getString("noOfAvailableUnits");
                    if (null != add && !add.equals("")) {
                        proj.noOfAvailableUnits = add;
                    }
                }
                if (projObj.has("noOfBlocks")) {
                    String add = projObj.getString("noOfBlocks");
                    if (null != add && !add.equals("")) {
                        proj.noOfBlocks = add;
                    }
                }
                if (projObj.has("noOfUnits")) {
                    String add = projObj.getString("noOfUnits");
                    if (null != add && !add.equals("")) {
                        proj.noOfUnits = add;
                    }
                }
                if (projObj.has("otherInfo")) {
                    String add = projObj.getString("otherInfo");
                    if (null != add && !add.equals("")) {
                        proj.otherInfo = add;
                    }
                }
                if (projObj.has("packageId")) {
                    String add = projObj.getString("packageId");
                    if (null != add && !add.equals("")) {
                        proj.packageId = add;
                    }
                }
                if (projObj.has("posessionDate")) {
                    String add = projObj.getString("posessionDate");
                    if (null != add && !add.equals("")) {
                        proj.posessionDate = add;
                    }
                }
                if (projObj.has("projectType")) {
                    String add = projObj.getString("projectType");
                    if (null != add && !add.equals("")) {
                        proj.projectType = add;
                    }
                }
                if (projObj.has("propertyTypes")) {
                    String add = projObj.getString("propertyTypes");
                    if (null != add && !add.equals("")) {
                        proj.propertyTypes = add;
                    }
                }
                if (projObj.has("status")) {
                    String add = projObj.getString("status");
                    if (null != add && !add.equals("")) {
                        proj.status = add;
                    }
                }
                if (projObj.has("summary")) {
                    JSONArray summaryArray = projObj.getJSONArray("summary");
                    if (null != summaryArray) {
                        ProjectInfo.Summary summary[] = new ProjectInfo.Summary[summaryArray
                                .length()];
                        for (int i = 0; i < summaryArray.length(); i++) {
                            JSONObject summaryObj = summaryArray
                                    .getJSONObject(i);
                            if (null != summaryObj) {
                                summary[i] = proj.new Summary();
                                if (summaryObj.has("area")) {
                                    String add = summaryObj.getString("area");
                                    if (null != add && !add.equals("")) {
                                        summary[i].area = add;
                                    }
                                }
                                if (summaryObj.has("bathrooms")) {
                                    String add = summaryObj
                                            .getString("bathrooms");
                                    if (null != add && !add.equals("")) {
                                        summary[i].bathrooms = add;
                                    }
                                }
                                if (summaryObj.has("bedroom")) {
                                    String add = summaryObj
                                            .getString("bedroom");
                                    if (null != add && !add.equals("")) {
                                        summary[i].bedroom = add;
                                    }
                                }
                                if (summaryObj.has("carParking")) {
                                    String add = summaryObj
                                            .getString("carParking");
                                    if (null != add && !add.equals("")) {
                                        summary[i].carParking = add;
                                    }
                                }
                                if (summaryObj.has("floorplanArray")) {
                                    JSONArray floorplanArray = summaryObj
                                            .getJSONArray("floorplanArray");
                                    if (null != floorplanArray) {
                                        for (int j = 0; j < floorplanArray
                                                .length(); j++) {
                                            summary[i].floorPlans[j] = floorplanArray
                                                    .getString(j);
                                        }
                                    }
                                }
                                if (summaryObj.has("landArea")) {
                                    String add = summaryObj
                                            .getString("landArea");
                                    if (null != add && !add.equals("")) {
                                        summary[i].landArea = add;
                                    }
                                }
                                if (summaryObj.has("noOfUnits")) {
                                    String add = summaryObj
                                            .getString("noOfUnits");
                                    if (null != add && !add.equals("")) {
                                        summary[i].noOfUnits = add;
                                    }
                                }
                                if (summaryObj.has("price")) {
                                    String add = summaryObj.getString("price");
                                    if (null != add && !add.equals("")) {
                                        summary[i].price = add;
                                    }
                                }
                                if (summaryObj.has("carParking")) {
                                    String add = summaryObj
                                            .getString("carParking");
                                    if (null != add && !add.equals("")) {
                                        summary[i].carParking = add;
                                    }
                                }
                                if (summaryObj.has("landArea")) {
                                    String add = summaryObj
                                            .getString("landArea");
                                    if (null != add && !add.equals("")) {
                                        summary[i].landArea = add;
                                    }
                                }
                                if (summaryObj.has("propertyType")) {
                                    String add = summaryObj
                                            .getString("propertyType");
                                    if (null != add && !add.equals("")) {
                                        summary[i].propertyType = add;
                                    }
                                }
                                if (summaryObj.has("noOfUnits")) {
                                    String add = summaryObj
                                            .getString("noOfUnits");
                                    if (null != add && !add.equals("")) {
                                        summary[i].noOfUnits = add;
                                    }
                                }
                                if (summaryObj.has("price")) {
                                    String add = summaryObj.getString("price");
                                    if (null != add && !add.equals("")) {
                                        summary[i].price = add;
                                    }
                                }
                                proj.summary = summary;
                            }
                        }
                    }
                }
                if (projObj.has("url")) {
                    String add = projObj.getString("url");
                    if (null != add && !add.equals("")) {
                        proj.url = add;
                    }
                }
                JSONArray videoArray = projObj.getJSONArray("videoLinks");
                if (null != videoArray) {
                    proj.videoLinks = new String[videoArray.length()];
                    for (int i = 0; i < videoArray.length(); i++) {
                        String str = videoArray.getString(i);
                        if (null != str && !str.equals("")) {
                            proj.videoLinks[i] = str;
                        }
                    }
                }
                JSONArray amenitiesArray = projObj.getJSONArray("amenities");
                if (null != amenitiesArray) {
                    proj.amenities = new String[amenitiesArray.length()];
                    for (int i = 0; i < amenitiesArray.length(); i++) {
                        String str = amenitiesArray.getString(i);
                        if (null != str && !str.equals("")) {
                            proj.amenities[i] = str;
                        }
                    }
                }
                if (projObj.has("approvalNumber")) {
                    String add = projObj.getString("approvalNumber");
                    if (null != add && !add.equals("")) {
                        proj.approvalNumber = add;
                    }
                }
                if (projObj.has("approvedBy")) {
                    String add = projObj.getString("approvedBy");
                    if (null != add && !add.equals("")) {
                        proj.approvedBy = add;
                    }
                }
                if (projObj.has("bankApprovals")) {
                    String add = projObj.getString("bankApprovals");
                    if (null != add && !add.equals("")) {
                        proj.bankApprovals = add;
                    }
                }
                if (projObj.has("builderCredaiStatus")) {
                    String add = projObj.getString("builderCredaiStatus");
                    if (null != add && !add.equals("")) {
                        proj.builderCredaiStatus = add;
                    }
                }
                if (projObj.has("builderDescription")) {
                    String add = projObj.getString("builderDescription");
                    if (null != add && !add.equals("")) {
                        proj.builderDescription = add;
                    }
                }
                if (projObj.has("builderId")) {
                    String add = projObj.getString("builderId");
                    if (null != add && !add.equals("")) {
                        proj.builderId = add;
                    }
                }
                if (projObj.has("builderLogo")) {
                    String add = projObj.getString("builderLogo");
                    if (null != add && !add.equals("")) {
                        proj.builderLogo = add;
                    }
                }
                if (projObj.has("builderName")) {
                    String add = projObj.getString("builderName");
                    if (null != add && !add.equals("")) {
                        proj.builderName = add;
                    }
                }
                if (projObj.has("builderUrl")) {
                    String add = projObj.getString("builderUrl");
                    if (null != add && !add.equals("")) {
                        proj.builderUrl = add;
                    }
                }
                if (projObj.has("electricityConnection")) {
                    String add = projObj.getString("electricityConnection");
                    if (null != add && !add.equals("")) {
                        proj.electricityConnection = add;
                    }
                }
                if (projObj.has("lastMileLandmark")) {
                    String add = projObj.getString("lastMileLandmark");
                    if (null != add && !add.equals("")) {
                        proj.lastMileLandmark = add;
                    }
                }
                if (projObj.has("lastMileLat")) {
                    String add = projObj.getString("lastMileLat");
                    if (null != add && !add.equals("")) {
                        proj.lastMileLat = add;
                    }
                }
                if (projObj.has("lastMileLon")) {
                    String add = projObj.getString("lastMileLon");
                    if (null != add && !add.equals("")) {
                        proj.lastMileLon = add;
                    }
                }
                if (projObj.has("lat")) {
                    String add = projObj.getString("lat");
                    if (null != add && !add.equals("")) {
                        proj.lat = add;
                    }
                }
                if (projObj.has("lon")) {
                    String add = projObj.getString("lon");
                    if (null != add && !add.equals("")) {
                        proj.lon = add;
                    }
                }
                if (projObj.has("lastMileLon")) {
                    String add = projObj.getString("lastMileLon");
                    if (null != add && !add.equals("")) {
                        proj.lastMileLon = add;
                    }
                }
                if (projObj.has("otherAmenities")) {
                    String add = projObj.getString("otherAmenities");
                    if (null != add && !add.equals("")) {
                        proj.otherAmenities = add;
                    }
                }
                if (projObj.has("otherBanks")) {
                    String add = projObj.getString("otherBanks");
                    if (null != add && !add.equals("")) {
                        proj.otherBanks = add;
                    }
                }
                if (projObj.has("specification")) {
                    String add = projObj.getString("specification");
                    if (null != add && !add.equals("")) {
                        proj.specification = add;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v("chandu", "" + proj);
            return proj;
        }

        private void downloadFirstImageAndSet(JSONObject jsonObject, int length) {
            Log.d(TAG, "downloadFirstImageAndSet");
            // TODO Auto-generated method stub
            try {
                publishProgress(downLoadImages(
                        new String[] { jsonObject.getString("reference") },
                        length));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // if(v.getId() == android.R.id.u)
    }

    private Drawable[] downLoadImages(String[] URLs, int length) {
        Log.d(TAG, "downloadFirstImageAndSet" + URLs);
        imageArray = new Drawable[length];
        InputStream in = null;
        try {
            for (int i = 0; i < URLs.length; i++) {
                in = OpenHttpConnection(URLs[i]);
                imageArray[i] = new BitmapDrawable(ProjectDetails.this
                        .getApplicationContext().getResources(),
                        BitmapFactory.decodeStream(in));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "downloadFirstImageAndSet:completed" + URLs);
        return imageArray;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Log.v("chandu", "back Pressed");
        super.onBackPressed();
        if (null != mProjectInfoTask)
            mProjectInfoTask.cancel(Boolean.TRUE);
        if (null != mDownloadImageTask) {
            mDownloadImageTask.cancel(Boolean.TRUE);
        }
        mProjectInfoTask = null;
        viewPager = null;
        adapter = null;
        imageArray = null;
        defaultArray = null;
        imageLinks = null;
        jsonObj = null;
    }

    private void updateUi(ProjectInfo p) {

    }

    public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        private int position;

        public DownloadImageTask(int pos) {
            this.position = pos;
        }

        @Override
        protected Drawable doInBackground(String... URLS) {
            Log.d(TAG, "DownloadImageTask:" + position);
            // TODO Auto-generated method stub
            Drawable drawable = null;// = new Drawable[length];
            InputStream in = null;
            try {
                // for (int i = 0; i < URLs.length; i++) {
                in = OpenHttpConnection(URLS[0]);
                drawable = new BitmapDrawable(ProjectDetails.this
                        .getApplicationContext().getResources(),
                        BitmapFactory.decodeStream(in));
                // }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "DownloadImageTask:Completed" + position);
            return drawable;

        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            if (null != result) {
                Log.d(TAG, "DownloadImageTask:onPostExecute" + result);
                Log.d(TAG, "DownloadImageTask:onPostExecute:Setting new image");
                imageArray[position] = result;
                ((ImagePagerAdapter) viewPager.getAdapter()).getHolderViews()[position]
                        .getView().setBackground(imageArray[position]);
            }
        }
    }

}
