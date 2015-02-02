package com.yahoo.netram.googleimagesearch.models;

import java.io.Serializable;

/**
 * Created by netram on 2/1/15.
 */
public class ImageSearchCriteria implements Serializable{

    private String size = "all";
    private String color = "all";
    private String type = "all";
    private String site = null;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
