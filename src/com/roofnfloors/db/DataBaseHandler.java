package com.roofnfloors.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.roofnfloors.db.DataBaseContent.ProjectDetailTable;
import com.roofnfloors.db.DataBaseContent.ProjectTable;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ProjectsManager";

    private String CREATE_PROJECTS_TABLE = "CREATE TABLE "
            + ProjectTable.TABLE_NAME + "(" + ProjectTable.KEY_PROJECT_ID
            + " TEXT PRIMARY KEY," + ProjectTable.KEY_NAME + " TEXT,"
            + ProjectTable.KEY_LAT + " DOUBLE," + ProjectTable.KEY_LON
            + " DOUBLE" + ")";

    private String CREATE_PROJECT_DETAILS_TABLE = "CREATE TABLE "
            + ProjectDetailTable.TABLE_NAME + "("
            + ProjectDetailTable.KEY_LISTING_ID + " TEXT PRIMARY KEY,"
            + ProjectDetailTable.KEY_BUILDER_NAME + " TEXT,"
            + ProjectDetailTable.KEY_CITY + " TEXT,"
            + ProjectDetailTable.KEY_ADDRESS1 + " TEXT,"
            + ProjectDetailTable.KEY_ADDRESS2 + " TEXT,"
            + ProjectDetailTable.KEY_AVAILABLEUNITS + " INTEGER,"
            + ProjectDetailTable.KEY_NOOFBLOCKS + " INTEGER,"
            + ProjectDetailTable.KEY_STATUS + " TEXT,"
            + ProjectDetailTable.KEY_PROJECTTYPE + " TEXT,"
            + ProjectDetailTable.KEY_POSSESION_DATE + " DOUBLE,"
            + ProjectDetailTable.KEY_MINPRICE_SQFT + " INTEGER,"
            + ProjectDetailTable.KEY_MAXPRICE_SQFT + " INTEGER,"
            + ProjectDetailTable.KEY_MAXAREA + " INTEGER,"
            + ProjectDetailTable.KEY_MINAREA + " TEXT,"
            + ProjectDetailTable.KEY_DESCRIPTION + " TEXT,"
            + ProjectDetailTable.KEY_SPECIFICATION + " TEXT,"
            + ProjectDetailTable.KEY_BUILDER_SPECIFICATION + " TEXT,"
            + ProjectDetailTable.KEY_BUILDER_URL + " TEXT,"
            + ProjectDetailTable.KEY_AMENITIES + " TEXT,"
            + ProjectDetailTable.KEY_OTHER_AMENITIES + " TEXT,"
            + ProjectDetailTable.KEY_IMAGE_URLS + " TEXT" + ")";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROJECTS_TABLE);
        db.execSQL(CREATE_PROJECT_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProjectTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProjectDetailTable.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
}