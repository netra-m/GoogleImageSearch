package com.yahoo.netram.googleimagesearch.activities;

import com.yahoo.netram.googleimagesearch.models.ImageSearchCriteria;

/**
 * Created by netram on 2/2/15.
 */
public interface SettingsCallBack {

    public void onSettingsSaved(ImageSearchCriteria imageSearchCriteria);
}
