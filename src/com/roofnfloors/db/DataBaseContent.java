package com.roofnfloors.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import com.roofnfloors.parser.Project;
import com.roofnfloors.parser.Project.ProjectDetails;
import com.roofnfloors.util.Utility;

public class DataBaseContent {
    private static final String TAG = DataBaseContent.class.getCanonicalName();

    public interface ProjectColumn {
        public static final String TABLE_NAME = "Projects";
        public static final String KEY_PROJECT_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_LAT = "latitude";
        public static final String KEY_LON = "longitude";
    }

    public final static class ProjectTable implements ProjectColumn {

        public final static ArrayList<Project> getAllProjects(Context context) {
            // Select All Query
            final String selectQuery = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = null;
            SQLiteDatabase db = null;
            ArrayList<Project> projectList = null;
            try {
                db = new DataBaseHandler(context).getWritableDatabase();
                cursor = db.rawQuery(selectQuery, new String[] {});

                // looping through all rows and adding to list
                if (cursor != null && cursor.moveToFirst()) {
                    projectList = new ArrayList<Project>();
                    do {
                        Project project = new Project(cursor.getString(cursor
                                .getColumnIndex(KEY_PROJECT_ID)));
                        project.setmLatitude(cursor.getDouble(cursor
                                .getColumnIndex(KEY_LAT)));
                        project.setmLongitude(cursor.getDouble(cursor
                                .getColumnIndex(KEY_LON)));
                        project.setmProjectName(cursor.getString(cursor
                                .getColumnIndex(KEY_NAME)));
                        // Adding contact to list
                        projectList.add(project);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }
            // return contact list
            return projectList;
        }

        public final static int getProjectsCount(Context context) {
            String countQuery = "SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db = new DataBaseHandler(context)
                    .getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = 0;
            if (cursor != null) {
                count = cursor.getCount();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
            return count;
        }

        // Adding new contact
        public final static void addProjects(ArrayList<Project> plist,
                Context context) {
            SQLiteDatabase db = new DataBaseHandler(context)
                    .getWritableDatabase();
            ContentValues values = new ContentValues();
            try {
                for (Project project : plist) {
                    values.put(ProjectTable.KEY_PROJECT_ID,
                            project.getmProjectid());
                    values.put(ProjectTable.KEY_NAME, project.getmProjectName());
                    values.put(ProjectTable.KEY_LAT, project.getmLatitude());
                    values.put(ProjectTable.KEY_LON, project.getmLongitude());
                    // Inserting Row
                    db.insert(TABLE_NAME, null, values);
                    values.clear();
                }
            } catch (Exception e) {
            } finally {
                Log.d("Test", TAG + "saveProjectList-end");
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
                if (values != null) {
                    values.clear();
                    values = null;
                }
            }
        }
    }

    public interface ProjectDetailColumn {
        public static final String TABLE_NAME = "ProjectDetails";
        public static final String KEY_LISTING_ID = "id";
        public static final String KEY_BUILDER_NAME = "builderName";
        public static final String KEY_ADDRESS1 = "address1";
        public static final String KEY_ADDRESS2 = "address2";
        public static final String KEY_CITY = "city";
        public static final String KEY_AVAILABLEUNITS = "availableUnits";
        public static final String KEY_NOOFBLOCKS = "noblocks";
        public static final String KEY_STATUS = "status";
        public static final String KEY_PROJECTTYPE = "projectType";
        public static final String KEY_POSSESION_DATE = "posessionDate";
        public static final String KEY_MINPRICE_SQFT = "minPricePerSqft";
        public static final String KEY_MAXPRICE_SQFT = "maxPricePerSqft";
        public static final String KEY_MAXAREA = "maxArea";
        public static final String KEY_MINAREA = "minArea";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_SPECIFICATION = "specification";
        public static final String KEY_BUILDER_SPECIFICATION = "builderDescription";
        public static final String KEY_BUILDER_URL = "builderUrl";
        public static final String KEY_AMENITIES = "amenities";
        public static final String KEY_OTHER_AMENITIES = "otherAmenities";
        public static final String KEY_IMAGE_URLS = "imageUrls";
        public static final String[] PROJECTION = new String[] { KEY_ADDRESS1,
                KEY_ADDRESS2, KEY_PROJECTTYPE, KEY_CITY, KEY_AVAILABLEUNITS,
                KEY_NOOFBLOCKS, KEY_STATUS, KEY_POSSESION_DATE,

                KEY_MINPRICE_SQFT, KEY_MAXPRICE_SQFT, KEY_MINAREA, KEY_MAXAREA,
                KEY_AMENITIES, KEY_DESCRIPTION, KEY_SPECIFICATION,
                KEY_BUILDER_SPECIFICATION, KEY_BUILDER_NAME, KEY_BUILDER_URL,
                KEY_OTHER_AMENITIES, KEY_IMAGE_URLS };
    }

    public final static class ProjectDetailTable implements ProjectDetailColumn {

        public final static Project getProjectInfo(String id, Context context) {

            SQLiteDatabase db = null;
            Cursor cursor = null;
            Project projectInfo = null;
            try {
                db = new DataBaseHandler(context).getReadableDatabase();

                cursor = db.query(TABLE_NAME, PROJECTION,
                        KEY_LISTING_ID + "=?", new String[] { id }, null, null,
                        null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        String addressLine1 = cursor.getString(cursor
                                .getColumnIndex(KEY_ADDRESS1));
                        String addressLine2 = cursor.getString(cursor
                                .getColumnIndex(KEY_ADDRESS2));
                        String city = cursor.getString(cursor
                                .getColumnIndex(KEY_CITY));
                        String description = cursor.getString(cursor
                                .getColumnIndex(KEY_DESCRIPTION));
                        String maxArea = cursor.getString(cursor
                                .getColumnIndex(KEY_MAXAREA));
                        String minArea = cursor.getString(cursor
                                .getColumnIndex(KEY_MINAREA));
                        String maxPricePerSqft = cursor.getString(cursor
                                .getColumnIndex(KEY_MAXPRICE_SQFT));
                        String minPricePerSqft = cursor.getString(cursor
                                .getColumnIndex(KEY_MINPRICE_SQFT));
                        String noOfAvailableUnits = cursor.getString(cursor
                                .getColumnIndex(KEY_AVAILABLEUNITS));
                        String noOfBlocks = cursor.getString(cursor
                                .getColumnIndex(KEY_NOOFBLOCKS));
                        String posessionDate = cursor.getString(cursor
                                .getColumnIndex(KEY_POSSESION_DATE));
                        String projectType = cursor.getString(cursor
                                .getColumnIndex(KEY_PROJECTTYPE));
                        String status = cursor.getString(cursor
                                .getColumnIndex(KEY_STATUS));
                        String amenities = cursor.getString(cursor
                                .getColumnIndex(KEY_AMENITIES));
                        String builderDescription = cursor.getString(cursor
                                .getColumnIndex(KEY_BUILDER_SPECIFICATION));
                        String builderName = cursor.getString(cursor
                                .getColumnIndex(KEY_BUILDER_NAME));
                        String builderUrl = cursor.getString(cursor
                                .getColumnIndex(KEY_BUILDER_URL));
                        String otherAmenities = cursor.getString(cursor
                                .getColumnIndex(KEY_OTHER_AMENITIES));
                        String specification = cursor.getString(cursor
                                .getColumnIndex(KEY_SPECIFICATION));
                        String imageUrls = cursor.getString(cursor
                                .getColumnIndex(KEY_IMAGE_URLS));
                        projectInfo = new Project(id);
                        ProjectDetails pDetail = projectInfo.new ProjectDetails();
                        projectInfo.setProjectDetails(pDetail);
                        pDetail.setAddressLine1(addressLine1);
                        pDetail.setAddressLine2(addressLine2);
                        pDetail.setCity(city);
                        pDetail.setDescription(description);
                        pDetail.setMaxArea(maxArea);
                        pDetail.setMinArea(minArea);
                        pDetail.setMinPricePerSqft(minPricePerSqft);
                        pDetail.setMaxPricePerSqft(maxPricePerSqft);
                        pDetail.setNoOfAvailableUnits(noOfAvailableUnits);
                        pDetail.setNoOfBlocks(noOfBlocks);
                        pDetail.setPosessionDate(posessionDate);
                        pDetail.setProjectType(projectType);
                        pDetail.setStatus(status);
                        pDetail.setAmenities(amenities);
                        pDetail.setBuilderDescription(builderDescription);
                        pDetail.setBuilderName(builderName);
                        pDetail.setBuilderUrl(builderUrl);
                        pDetail.setOtherAmenities(otherAmenities);
                        pDetail.setSpecification(specification);
                        pDetail.setImageUrls(imageUrls);
                    }
                }
            } catch (Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }

            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }
            return projectInfo;
        }

        public final static void addProjectDetails(Project projectInfo,
                Context context) {
            if (projectInfo == null)
                return;
            SQLiteDatabase db = null;
            ContentValues values = new ContentValues();
            try {
                db = new DataBaseHandler(context).getWritableDatabase();

                values.put(KEY_ADDRESS1, projectInfo.getProjectDetails()
                        .getAddressLine1());
                values.put(KEY_ADDRESS2, projectInfo.getProjectDetails()
                        .getAddressLine2());
                values.put(KEY_CITY, projectInfo.getProjectDetails().getCity());
                values.put(KEY_DESCRIPTION, projectInfo.getProjectDetails()
                        .getDescription());
                values.put(KEY_LISTING_ID, projectInfo.getProjectDetails()
                        .getListingId());
                values.put(KEY_MAXAREA, projectInfo.getProjectDetails()
                        .getMaxArea());
                values.put(KEY_MINAREA, projectInfo.getProjectDetails()
                        .getMinArea());
                values.put(KEY_MAXPRICE_SQFT, projectInfo.getProjectDetails()
                        .getMaxPricePerSqft());
                values.put(KEY_MINPRICE_SQFT, projectInfo.getProjectDetails()
                        .getMinPricePerSqft());
                values.put(KEY_AVAILABLEUNITS, projectInfo.getProjectDetails()
                        .getNoOfAvailableUnits());
                values.put(KEY_NOOFBLOCKS, projectInfo.getProjectDetails()
                        .getNoOfUnits());
                values.put(KEY_POSSESION_DATE, projectInfo.getProjectDetails()
                        .getPosessionDate());
                values.put(KEY_PROJECTTYPE, projectInfo.getProjectDetails()
                        .getProjectType());
                values.put(KEY_STATUS, projectInfo.getProjectDetails()
                        .getStatus());
                values.put(KEY_AMENITIES, projectInfo.getProjectDetails()
                        .getAmenities());
                if (Utility.isValidString(projectInfo.getProjectDetails()
                        .getBuilderDescription())) {
                    values.put(
                            KEY_BUILDER_SPECIFICATION,
                            ""
                                    + Html.fromHtml(projectInfo
                                            .getProjectDetails()
                                            .getBuilderDescription()));
                }
                values.put(KEY_BUILDER_NAME, projectInfo.getProjectDetails()
                        .getBuilderName());
                values.put(KEY_BUILDER_URL, projectInfo.getProjectDetails()
                        .getBuilderUrl());
                values.put(KEY_OTHER_AMENITIES, projectInfo.getProjectDetails()
                        .getOtherAmenities());
                if (Utility.isValidString(projectInfo.getProjectDetails()
                        .getSpecification())) {
                    values.put(
                            KEY_SPECIFICATION,
                            ""
                                    + Html.fromHtml(projectInfo
                                            .getProjectDetails()
                                            .getSpecification()));
                }
                values.put(KEY_IMAGE_URLS, projectInfo.getProjectDetails()
                        .getImageUrls());
                db.insert(TABLE_NAME, null, values);
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
                if (values != null) {
                    values.clear();
                    values = null;
                }
            }
        }
    }
}