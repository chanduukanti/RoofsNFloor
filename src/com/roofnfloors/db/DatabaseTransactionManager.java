package com.roofnfloors.db;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.roofnfloors.db.DataBaseContent.ProjectDetailTable;
import com.roofnfloors.db.DataBaseContent.ProjectTable;
import com.roofnfloors.parser.Project;

public class DatabaseTransactionManager {
    // util class
    private static final String TAG = DatabaseTransactionManager.class.getCanonicalName();
    private DatabaseTransactionManager() {

    }

    public static void saveProjectInfo(final Project p, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProjectDetailTable.addProjectDetails(p, context);
            }
        }).start();
    }

    public static void saveProjectList(final ArrayList<Project> plist,
            final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Test", TAG + "saveProjectList-start");
                ProjectTable.addProjects(plist, context);
            }
        }).start();
    }

}