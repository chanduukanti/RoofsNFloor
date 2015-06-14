package com.roofnfloors.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.roofnfloors.R;
import com.roofnfloors.adapter.ImagePagerAdapter;
import com.roofnfloors.loader.ImageLoader;
import com.roofnfloors.loader.ImageLoader.ImageLoaderCallBack;
import com.roofnfloors.parser.Project;
import com.roofnfloors.tasks.ProjectDataFetcherTask;
import com.roofnfloors.tasks.ProjectDataFetcherTask.ProjectDataFetcherCallBack;
import com.roofnfloors.util.Constants.DATA_LOAD_TYPE;
import com.roofnfloors.util.Constants.FETCH_SERVER_DATA_TYPE;
import com.roofnfloors.util.Utility;

public class ProjectDetailsActivity extends Activity implements
        ImageLoaderCallBack, ProjectDataFetcherCallBack {
    private static final String TAG = "ProjectDetails";

    // UI elements
    private TextView projectNametext;
    private TextView addressDetails;
    private TextView propertytypevalue;
    private TextView cityValue;
    private TextView blocksValue;
    private TextView availableunitsvalue;
    private TextView statusValue;
    private TextView possesionDate;
    private TextView priceperunitvalue;
    private TextView coveredAreaValue;
    private TextView descriptiontext;
    private TextView builderdescriptiontext;
    private TextView buildernametext;
    private Button builderurlBtn;// Btn
    private ActionBar mActionbar;
    private TextView amenitiesText;

    private ViewPager mViewPager;
    private Drawable[] mImageArray;
    private String projectName;
    public String json;
    private Project mCurrentProject;
    private String mCurrentProjectId;
    private ImagePagerAdapter mAdapter;
    private ImageLoader mImageLoader = ImageLoader.getInstance(this);
    private ProjectDataFetcherTask mProjectDataFetcherTask;
    private ImagePagerAdapter mDefaultAdapter;
    private int mCurrentImagePosition;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_item_details);
        Intent detailIntent = getIntent();
        mImageArray = new Drawable[] { this.getResources().getDrawable(
                R.drawable.roofnfloor_default) };
        mDefaultAdapter = new ImagePagerAdapter(this, mImageArray);
        mCurrentProjectId = detailIntent.getStringExtra("projectId");
        mActionbar = getActionBar();
        mActionbar.setTitle(projectName);
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setDisplayShowHomeEnabled(true);

        inItViews();
        mCurrentImagePosition = 0;
        mViewPager.setOnPageChangeListener(new ImagePagerHandler());
        mViewPager.setAdapter(mDefaultAdapter);
        startTask();
    }

    private void startTask() {
        mProjectDataFetcherTask = new ProjectDataFetcherTask(this,
                FETCH_SERVER_DATA_TYPE.PROJECT_DETAIL);
        mProjectDataFetcherTask.execute(Utility
                .getUrlWithAppendedProjectId(mCurrentProjectId));
    }

    private void handleImageAdapter(DATA_LOAD_TYPE loadType){
        if(mCurrentProject != null && loadType == DATA_LOAD_TYPE.DATABASE){
            mImageArray = new Drawable[mCurrentProject.getProjectDetails().getImageUrls().split(";").length];
            for (int i = 0; i < mImageArray.length; i++) {
                mImageArray[i] = this.getResources().getDrawable(
                        R.drawable.roofnfloor_default);
            }
            createAndSetImageAdapter(mImageArray);
            fetchImage(mCurrentProject.getProjectDetails().getImageUrls().split(";")[0]);
        }
    }
    private void createAndSetImageAdapter(Drawable[] mArray) {
        mAdapter = new ImagePagerAdapter(ProjectDetailsActivity.this, mArray);
        if (mViewPager != null)
            mViewPager.setAdapter(mAdapter);
    }

    private void inItViews() {
        mViewPager = (ViewPager) findViewById(R.id.image_pager);
        projectNametext = (TextView) findViewById(R.id.project_name1);
        addressDetails = (TextView) findViewById(R.id.add_details);
        propertytypevalue = (TextView) findViewById(R.id.property_type_value);
        cityValue = (TextView) findViewById(R.id.city_value);
        availableunitsvalue = (TextView) findViewById(R.id.availableunitsvalue);
        blocksValue = (TextView) findViewById(R.id.blocks_value);
        statusValue = (TextView) findViewById(R.id.statusValue);
        possesionDate = (TextView) findViewById(R.id.possesionDate);
        priceperunitvalue = (TextView) findViewById(R.id.priceperunitvalue);
        coveredAreaValue = (TextView) findViewById(R.id.coveredAreaValue);

        amenitiesText = (TextView) findViewById(R.id.amenitiesText);

        descriptiontext = (TextView) findViewById(R.id.descriptiontext);
        builderdescriptiontext = (TextView) findViewById(R.id.builderdescriptiontext);
        buildernametext = (TextView) findViewById(R.id.buildernametext);
        builderurlBtn = (Button) findViewById(R.id.builderurltext);

    }

    @Override
    protected void onStop() {
        cleanUp();
        super.onStop();
    }

    private void fetchImage(String URL) {
        if (mImageLoader == null)
            mImageLoader = ImageLoader.getInstance(this);
        mImageLoader.setmImagePosition(mCurrentImagePosition);
        mImageLoader.displayImage(URL);
    }

    private void updateImage(int pos, Drawable image) {
        mImageArray[pos] = image;
        mAdapter.getHolderViews()[pos].getView().setImageDrawable(image);
        mAdapter.notifyDataSetChanged();
    }

    public void populateDataOnUI() {
        projectNametext.setText(projectName);
        String builderName = mCurrentProject.getProjectDetails()
                .getBuilderName() == null ? "" : mCurrentProject
                .getProjectDetails().getBuilderName();

        addressDetails.setText("By " + builderName + "\n"
                + mCurrentProject.getProjectDetails().getAddressLine1());

        propertytypevalue.setText(mCurrentProject.getProjectDetails()
                .getProjectType());
        if (Utility
                .isValidString(mCurrentProject.getProjectDetails().getCity())) {
            cityValue.setText(mCurrentProject.getProjectDetails().getCity());
        } else {
            cityValue.setText("-");
        }
        if (Utility.isValidString(mCurrentProject.getProjectDetails()
                .getNoOfAvailableUnits()))
            availableunitsvalue.setText(mCurrentProject.getProjectDetails()
                    .getNoOfAvailableUnits());
        else {
            availableunitsvalue.setText("-");
        }
        if (Utility.isValidString(mCurrentProject.getProjectDetails()
                .getNoOfBlocks()))
            blocksValue.setText(mCurrentProject.getProjectDetails()
                    .getNoOfBlocks());
        else {
            blocksValue.setText("-");
        }

        statusValue.setText(mCurrentProject.getProjectDetails().getStatus());

        possesionDate.setText(mCurrentProject.getProjectDetails()
                .getPosessionDate());

        priceperunitvalue.setText(mCurrentProject.getProjectDetails()
                .getMinPricePerSqft()
                + "-"
                + mCurrentProject.getProjectDetails().getMaxPricePerSqft());

        coveredAreaValue.setText(mCurrentProject.getProjectDetails()
                .getMinArea()
                + "-"
                + mCurrentProject.getProjectDetails().getMaxArea());

        descriptiontext.setText(mCurrentProject.getProjectDetails()
                .getDescription());

        builderdescriptiontext.setText(mCurrentProject.getProjectDetails()
                .getBuilderDescription());

        buildernametext.setText(builderName);

        builderurlBtn.setText(mCurrentProject.getProjectDetails()
                .getBuilderUrl());
        StringBuilder amentiestext = new StringBuilder();
        if (Utility.isValidString(mCurrentProject.getProjectDetails()
                .getAmenities())) {
            amentiestext.append(mCurrentProject.getProjectDetails()
                    .getAmenities());
        }
        if (Utility.isValidString(mCurrentProject.getProjectDetails()
                .getOtherAmenities())) {
            amentiestext.append(mCurrentProject.getProjectDetails()
                    .getOtherAmenities());
        }
        if (Utility.isValidString(amentiestext.toString())) {
            amenitiesText.setText(amentiestext.toString());
        } else {
            amenitiesText.setText("-");
        }

    }

    @Override
    public void onBackPressed() {
        cleanUp();
        super.onBackPressed();
    }

    private class ImagePagerHandler implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected:" + position);
            mCurrentImagePosition = position;
            if (mCurrentProject != null) {
                fetchImage(getImageUrlAtPosition());
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }

    @Override
    public void onFirstImageDownloaded(final Bitmap map, final int length) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (length != 0) {
                    mImageArray = new Drawable[length];
                    mImageArray[0] = Utility.bitmapToDrawable(
                            ProjectDetailsActivity.this, map);
                    for (int i = 1; i < mImageArray.length; i++) {
                        mImageArray[i] = getResources().getDrawable(
                                R.drawable.roofnfloor_default);
                    }
                    createAndSetImageAdapter(mImageArray);
                }
            }
        });
        // updateImage(0, mImageArray[0]);
    }

    private void cleanUp() {
        if (mImageLoader != null) {
            mImageLoader.stopImageLoader();
            mImageLoader = null;
        }
        if (null != mProjectDataFetcherTask) {
            mProjectDataFetcherTask.cancel(Boolean.TRUE);
            mProjectDataFetcherTask = null;
        }
        mDefaultAdapter = null;
        mCurrentProject = null;
        mImageLoader = null;
        mViewPager = null;
        mAdapter = null;

    }

    @Override
    public void onProjectListTaskCompleted(ArrayList<Project> plist) {
        // do nothing
    }

    @Override
    public void onProjectDetailsTaskCompleted(Project pinfo, DATA_LOAD_TYPE loadType) {
        if (pinfo != null) {
            mCurrentProject = pinfo;
            handleImageAdapter(loadType);
            populateDataOnUI();
        }
    }

    private String getImageUrlAtPosition() {
        return mCurrentProject.getProjectDetails().getImageUrls().split(";")[mCurrentImagePosition];
    }

    public void onImageLoadingCompleted(Bitmap map, final int pos) {
        if (map != null) {
            final Drawable image = Utility.bitmapToDrawable(this, map);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    updateImage(pos, image);
                }
            });
        }
    }
}
