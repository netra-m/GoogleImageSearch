package com.yahoo.netram.googleimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by netram on 2/1/15.
 */
public class ImageResult implements Serializable{

    private String thumbNailUrl;
    private String fullUrl;
    private String title;

    public ImageResult(JSONObject jsonObject) {

        try {
            this.thumbNailUrl = jsonObject.getString("tbUrl");
            this.fullUrl = jsonObject.getString("url");
            this.title = jsonObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> getFromJSONArray(JSONArray jsonArray) {
        ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                imageResults.add(new ImageResult(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return imageResults;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
