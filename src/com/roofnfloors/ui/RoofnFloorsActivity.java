package com.roofnfloors.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.android.roofnfloors.R;
import com.google.android.gms.maps.model.LatLng;
import com.roofnfloors.adapter.TabsPagerAdapter;
import com.roofnfloors.parser.Project;
import com.roofnfloors.tasks.ProjectDataFetcherTask;
import com.roofnfloors.tasks.ProjectDataFetcherTask.ProjectDataFetcherCallBack;
import com.roofnfloors.util.Constants;
import com.roofnfloors.util.Constants.DATA_LOAD_TYPE;
import com.roofnfloors.util.Constants.FETCH_SERVER_DATA_TYPE;

@SuppressWarnings("deprecation")
public class RoofnFloorsActivity extends FragmentActivity implements
        ActionBar.TabListener, ProjectDataFetcherCallBack {
    private static final String TAG = RoofnFloorsActivity.class
            .getCanonicalName();
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private ArrayList<Project> mProjectList;
    private UIHandler mUIHandler;
    private HashMap<LatLng, Project> myProjectsMap = new HashMap<LatLng, Project>();
    boolean isNetworkAvailable = false;
    public Context mContext;
    private ProjectDataFetcherTask mDownloadProjectListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roof_floors_main);
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mContext = this;
        viewPager.setAdapter(mAdapter);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : Constants.TABS) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        init();
    }

    private void init() {
        mUIHandler = new UIHandler();
        mDownloadProjectListTask = new ProjectDataFetcherTask(this,
                FETCH_SERVER_DATA_TYPE.PROJECT_LIST);
        mDownloadProjectListTask.execute(Constants.PROJECT_LIST_URL);
    }

    public void makeMyprojects(ArrayList<Project> projectList) {
        if (projectList != null) {
            setmProjectList(projectList);
            for (int i = 0; i < projectList.size(); i++) {
                LatLng latlang = new LatLng(projectList.get(i).getmLatitude(),
                        projectList.get(i).getmLongitude());
                myProjectsMap.put(latlang, projectList.get(i));
            }
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public HashMap<LatLng, Project> getMyProjectsMap() {
        return myProjectsMap;
    }

    public void setMyProjectsMap(HashMap<LatLng, Project> myProjectsMap) {
        this.myProjectsMap = myProjectsMap;
    }

    public ArrayList<Project> getmProjectList() {
        return mProjectList;
    }

    public void setmProjectList(ArrayList<Project> mProjectList) {
        this.mProjectList = mProjectList;
    }

    @Override
    protected void onStop() {
        cleanUp();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        cleanUp();
        super.onBackPressed();
    }

    private void cleanUp() {
        if (mDownloadProjectListTask != null) {
            mDownloadProjectListTask.cancel(Boolean.FALSE);
            mDownloadProjectListTask = null;
        }
    }

    @Override
    public void onProjectListTaskCompleted(ArrayList<Project> plist) {
        Log.d("Test", TAG + "onProjectListTaskCompleted");
        if (plist != null) {
            makeMyprojects(plist);
            UIHandler mUiHandler = getmUiHandler();
            if (mUiHandler != null) {
                Log.d("Test", TAG + "mUiHandler not null");
                ListViewFragment mList = mUiHandler.getmListViewFragment();
                if (mList != null) {
                    Log.d("Test", TAG + "ListViewFragment not null");
                    mList.onProjectListDataReceived(plist);
                }
                MapViewFragment mMap = mUIHandler.getmMapViewFragment();
                if (mMap != null) {
                    mMap.onProjectListDataReceived(plist);
                }
            }
        }
    }

    @Override
    public void onProjectDetailsTaskCompleted(Project pinfo, DATA_LOAD_TYPE loadtype) {
        // do nothing
    }

    class UIHandler {
        private ListViewFragment mListViewFragment;
        private RoofnFloorsActivity mActivity;
        private MapViewFragment mMapViewFragment;
        private Handler mHandler = new Handler();

        UIHandler() {
            mActivity = RoofnFloorsActivity.this;
        }

        public ListViewFragment getmListViewFragment() {
            return mListViewFragment;
        }

        public void setmListViewFragment(ListViewFragment mListViewFragment) {
            this.mListViewFragment = mListViewFragment;
        }

        public RoofnFloorsActivity getmActivity() {
            return mActivity;
        }

        public void setmActivity(RoofnFloorsActivity mActivity) {
            this.mActivity = mActivity;
        }

        public MapViewFragment getmMapViewFragment() {
            return mMapViewFragment;
        }

        public void setmMapViewFragment(MapViewFragment mMapViewFragment) {
            this.mMapViewFragment = mMapViewFragment;
        }

        public Handler getmHandler() {
            return mHandler;
        }

        public void setmHandler(Handler mHandler) {
            this.mHandler = mHandler;
        }
    }

    public UIHandler getmUiHandler() {
        return mUIHandler;
    }

    public void setmUiHandler(UIHandler mUiHandler) {
        this.mUIHandler = mUiHandler;
    }

    @Override
    public void onFirstImageDownloaded(Bitmap map, int length) {
        //do nothing
    }
}
