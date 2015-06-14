package com.roofnfloors.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.roofnfloors.db.DatabaseTransactionManager;
import com.roofnfloors.net.ServerDataFetcher;
import com.roofnfloors.parser.Project.ProjectDetails;
import com.roofnfloors.parser.Project.ProjectDetails.Documents;
import com.roofnfloors.tasks.ProjectDataFetcherTask.ProjectDataFetcherCallBack;
import com.roofnfloors.util.Utility;

public class JSONParser {
    private static final String TAG = JSONParser.class.getCanonicalName();

    public static ArrayList<Project> parseProjectList(String string,
            Context context) {
        Log.d("Test", TAG + "parseProjectList-start");
        ArrayList<Project> pList = null;
        JSONObject c = null;
        String id;
        String pname;
        Double lat;
        Double longg;
        Project p;
        JSONArray jarr;
        try {
            jarr = new JSONArray(string);
            pList = new ArrayList<Project>();
            for (int i = 0; i < jarr.length(); i++) {
                c = jarr.getJSONObject(i);
                id = c.getString("id");
                pname = c.getString("projectName");
                lat = c.getDouble("lat");
                longg = c.getDouble("lon");
                p = new Project(id);
                p.setmProjectName(pname);
                p.setmLatitude(lat);
                p.setmLongitude(longg);
                pList.add(p);
            }
            Log.d("Test", TAG + "parseProjectList-done");
            if (pList != null && pList.size() != 0) {
                DatabaseTransactionManager.saveProjectList(pList, context);
            }
        } catch (JSONException e) {
            Log.v(TAG, "JSON Exception error occured");
            e.printStackTrace();
        } finally {
            p = null;
            jarr = null;
            c = null;
        }
        return pList;
    }

    public static Project parseProjectDetails(String string, String urlProject,
            ProjectDataFetcherCallBack mCallBack) {
        Log.d("Test", TAG + "parseProjectDetails-start");
        Project project = new Project(Utility.getProjectIdFromUrl(urlProject));
        ProjectDetails pDetail = project.new ProjectDetails();
        project.setProjectDetails(pDetail);
        try {
            JSONObject projObj = new JSONObject(string);
            if (projObj.has("documents")) {
                JSONArray documents = projObj.getJSONArray("documents");
                if (null != documents) {
                    ProjectDetails.Documents docs[] = new ProjectDetails.Documents[documents
                            .length()];
                    JSONObject docu1 = documents.getJSONObject(0);
                    if (docu1 != null) {
                        String url = docu1.getString("reference");
                        if (url != null) {
                            ServerDataFetcher.downloadFirstImage(url, docs.length, mCallBack);
                        }
                    }
                    pDetail.setDocuments(docs);
                    String[] array;
                    JSONObject docu;
                    for (int i = 0; i < documents.length(); i++) {
                        array = new String[documents.length()];
                        docu = documents.getJSONObject(i);
                        docs[i] = pDetail.new Documents();
                        if (docu.has("reference")) {
                            String imageUrl = docu.getString("reference");
                            if (null != imageUrl && !imageUrl.equals("")) {
                                docs[i].reference = imageUrl;
                                array[i] = imageUrl;
                            }
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Documents document : docs) {
                        sb.append(document.toString() + ";");
                    }
                    pDetail.setImageUrls(sb.toString());
                }
                if (projObj.has("addressLine1")) {
                    String add = projObj.getString("addressLine1");
                    if (null != add && !add.equals("")) {
                        pDetail.setAddressLine1(add);
                    }
                }
                if (projObj.has("addressLine2")) {
                    String add = projObj.getString("addressLine2");
                    if (null != add && !add.equals("")) {
                        pDetail.setAddressLine2(add);
                    }
                }

                if (projObj.has("city")) {
                    String add = projObj.getString("city");
                    if (null != add && !add.equals("")) {
                        pDetail.setCity(add);
                    }
                }
                if (projObj.has("description")) {
                    String add = projObj.getString("description");
                    if (null != add && !add.equals("")) {
                        pDetail.setDescription(add);
                    }
                }

            }
            if (projObj.has("listingId")) {
                String add = projObj.getString("listingId");
                if (null != add && !add.equals("")) {
                    pDetail.setListingId(add);
                }
            }
            if (projObj.has("maxArea")) {
                String add = projObj.getString("maxArea");
                if (null != add && !add.equals("")) {
                    pDetail.setMaxArea(add);
                }
            }
            if (projObj.has("maxPrice")) {
                String add = projObj.getString("maxPrice");
                if (null != add && !add.equals("")) {
                    pDetail.setMaxArea(add);
                }
            }
            if (projObj.has("maxPricePerSqft")) {
                String add = projObj.getString("maxPricePerSqft");
                if (null != add && !add.equals("")) {
                    pDetail.setMaxPricePerSqft(add);
                }
            }
            if (projObj.has("minArea")) {
                String add = projObj.getString("minArea");
                if (null != add && !add.equals("")) {
                    pDetail.setMinArea(add);
                }
            }
            if (projObj.has("minPrice")) {
                String add = projObj.getString("minPrice");
                if (null != add && !add.equals("")) {
                    pDetail.setMaxPrice(add);
                }
            }
            if (projObj.has("minPricePerSqft")) {
                String add = projObj.getString("minPricePerSqft");
                if (null != add && !add.equals("")) {
                    pDetail.setMinPricePerSqft(add);
                }
            }
            if (projObj.has("noOfAvailableUnits")) {
                String add = projObj.getString("noOfAvailableUnits");
                if (null != add && !add.equals("") && "null".equals(add)) {
                    pDetail.setNoOfAvailableUnits(add);
                } else {
                    pDetail.setNoOfAvailableUnits("-");
                }
            }
            if (projObj.has("noOfBlocks")) {
                String add = projObj.getString("noOfBlocks");
                if (null != add && !add.equals("")) {
                    if (!add.equals("null")) {
                        pDetail.setNoOfBlocks(add);
                    }

                } else {
                    pDetail.setNoOfBlocks("-");
                }
            }
            if (projObj.has("noOfUnits")) {
                String add = projObj.getString("noOfUnits");
                if (null != add && !add.equals("")) {
                    if (!add.equals("null")) {
                        pDetail.setNoOfUnits(add);
                    }
                } else {
                    pDetail.setNoOfUnits("-");
                }
            }
            if (projObj.has("posessionDate")) {
                String add = projObj.getString("posessionDate");
                if (null != add && !add.equals("")) {
                    if (!add.equals("null")) {
                        long date = Long.parseLong(add);
                        Date itemDate = new Date(date);
                        String itemDateStr = new SimpleDateFormat("dd-MM-yyyy")
                                .format(itemDate);
                        pDetail.setPosessionDate(itemDateStr);
                    } else {
                        pDetail.setPosessionDate("---");
                    }
                }
            }
            if (projObj.has("projectType")) {
                String add = projObj.getString("projectType");
                if (null != add && !add.equals("")) {
                    pDetail.setProjectType(add);
                }
            }
            if (projObj.has("status")) {
                String add = projObj.getString("status");
                if (null != add && !add.equals("")) {
                    pDetail.setStatus(add);
                }
            }

            JSONArray amenitiesArray = projObj.getJSONArray("amenities");
            if (null != amenitiesArray) {
                String amenties[] = new String[amenitiesArray.length()];
                for (int i = 0; i < amenitiesArray.length(); i++) {
                    amenties[i] = amenitiesArray.getString(i);
                }
                pDetail.setAmenities(Arrays.toString(amenties));
            }

            if (projObj.has("builderDescription")) {
                String add = projObj.getString("builderDescription");
                if (null != add && !add.equals("")) {
                    add = "" + Html.fromHtml(add);
                    pDetail.setBuilderDescription(add);
                }
            }

            if (projObj.has("builderLogo")) {
                String add = projObj.getString("builderLogo");
                if (null != add && !add.equals("")) {
                    pDetail.setBuilderLogo(add);
                }
            }
            if (projObj.has("builderName")) {
                String add = projObj.getString("builderName");
                if (null != add && !add.equals("")) {
                    pDetail.setBuilderName(add);
                }
            }
            if (projObj.has("builderUrl")) {
                String add = projObj.getString("builderUrl");
                if (null != add && !add.equals("")) {
                    pDetail.setBuilderUrl(add);
                }
            }
            if (projObj.has("otherAmenities")) {
                String add = projObj.getString("otherAmenities");
                if (null != add && !add.equals("")) {
                    pDetail.setOtherAmenities(add);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Test", TAG + "parseProjectDetails-end");

        return project;
    }

}
