package com.roofnfloors.tasks;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.roofnfloors.db.DataBaseContent.ProjectDetailTable;
import com.roofnfloors.db.DataBaseContent.ProjectTable;
import com.roofnfloors.db.DatabaseTransactionManager;
import com.roofnfloors.net.ServerDataFetcher;
import com.roofnfloors.parser.JSONParser;
import com.roofnfloors.parser.Project;
import com.roofnfloors.util.Constants.DATA_LOAD_TYPE;
import com.roofnfloors.util.Constants.FETCH_SERVER_DATA_TYPE;
import com.roofnfloors.util.Utility;

public class ProjectDataFetcherTask extends AsyncTask<String, String, Void> {

    private Activity mContext;
    private ProjectDataFetcherCallBack mCallBack;
    private FETCH_SERVER_DATA_TYPE mTaskType;
    private ArrayList<Project> mProjectList;
    private Project mProjectInfo;
    private DATA_LOAD_TYPE mDataLoadType;
    private static final String TAG = ProjectDataFetcherTask.class.getCanonicalName();
    public ProjectDataFetcherTask(ProjectDataFetcherCallBack callBack,
            FETCH_SERVER_DATA_TYPE type) {
        super();
        this.mContext = (Activity)callBack;
        this.mCallBack = callBack;
        this.mTaskType = type;
    }

    public interface ProjectDataFetcherCallBack {
        public void onProjectListTaskCompleted(ArrayList<Project> plist);

        public void onProjectDetailsTaskCompleted(Project pinfo, DATA_LOAD_TYPE loadtype);

        void onFirstImageDownloaded(Bitmap map, int length);

    }

    protected Void doInBackground(String... urls) {
        switch (mTaskType) {
        case PROJECT_DETAIL:
            return fetchProjectDetail(urls[0]);
        case PROJECT_LIST:
            return fetchProjectList(urls[0]);
        default:
            return null;
        }
    }

    private Void fetchProjectDetail(String url) {
        Log.d("Test", TAG + "fetchProjectDetail");
        mProjectInfo = ProjectDetailTable.getProjectInfo(
                Utility.getProjectIdFromUrl(url), mContext);
        if(mProjectInfo==null){
            mDataLoadType = DATA_LOAD_TYPE.SERVER;
            Log.d("Test", TAG + "data not found in DB");
            String response = ServerDataFetcher.getServerResult(url);
            mProjectInfo = JSONParser.parseProjectDetails(response, url, mCallBack);
            if (mProjectInfo != null) {
                DatabaseTransactionManager.saveProjectInfo(mProjectInfo, mContext);
            }
        }else{
            mDataLoadType = DATA_LOAD_TYPE.DATABASE;
            Log.d("Test", TAG + "data found in DB");
        }
        return null;
    }

    private Void fetchProjectList(String url) {
        Log.d("Test", TAG + "fetchProjectList");
        mProjectList = ProjectTable
                .getAllProjects(mContext);
        if (mProjectList == null) {
            mDataLoadType = DATA_LOAD_TYPE.SERVER;
            Log.d("Test", TAG + "data Not Found in DB");
            String response = ServerDataFetcher.getServerResult(url);
            mProjectList = JSONParser.parseProjectList(response, mContext);
        }else{
            mDataLoadType = DATA_LOAD_TYPE.DATABASE;
            Log.d("Test", TAG + "data Found in DB");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        switch (mTaskType) {
        case PROJECT_LIST:
            mCallBack.onProjectListTaskCompleted(mProjectList);
            break;
        case PROJECT_DETAIL:
            mCallBack.onProjectDetailsTaskCompleted(mProjectInfo, mDataLoadType);
            break;
        default:
            break;
        }
    }
}
