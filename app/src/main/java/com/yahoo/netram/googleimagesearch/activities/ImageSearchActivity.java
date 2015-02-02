package com.yahoo.netram.googleimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.netram.googleimagesearch.R;
import com.yahoo.netram.googleimagesearch.adapters.ImageResultsAdapter;
import com.yahoo.netram.googleimagesearch.models.ImageResult;
import com.yahoo.netram.googleimagesearch.models.ImageSearchCriteria;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ImageSearchActivity extends ActionBarActivity {

    private static final int REQUEST_CODE = 333;
    private String query;
    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    private ImageSearchCriteria imageSearchCriteria;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        imageSearchCriteria = new ImageSearchCriteria();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(imageResultsAdapter);
    }

    private void setupViews() {
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ImageSearchActivity.this, ImageDisplayActivity.class);
                ImageResult itemClicked = imageResults.get(position);
                i.putExtra("imageResult", itemClicked);
                startActivity(i);

            }
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        if (offset >= 64) {
            return;
        }

        String imageSearchUrl = getImageSearchUrlWithQueryParams();
        imageSearchUrl = imageSearchUrl + "&start=" + offset;

        getImageResultsFromGoogle(imageSearchUrl, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.miSettings) {
            Intent i = new Intent(this, SearchSettingsActivity.class);
            i.putExtra("searchCriteria", imageSearchCriteria);
            startActivityForResult(i, REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View view) {

        getImageSearchResults();

    }

    private void getImageSearchResults() {
        String searchUrl = getImageSearchUrlWithQueryParams();

        if (searchUrl != null) {
            getImageResultsFromGoogle(searchUrl, true);
        }
    }

    private void getImageResultsFromGoogle(String searchUrl, final boolean clearPreviousCollection) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Unable to access internet", Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    if (clearPreviousCollection) {
                        imageResults.clear();
                        imageResultsAdapter.clear();
                    }
                    imageResultsAdapter.addAll(ImageResult.getFromJSONArray(imageResultsJson));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error", "JSONException:", e);
                }

                Log.d("DEBUG", imageResults.toString());
            }
        });
    }

    private String getImageSearchUrlWithQueryParams() {
        if (query == null || query.equals("")) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return null;
        }

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        if (!imageSearchCriteria.getSize().equals("all")) {
            String size = "medium";
            switch (imageSearchCriteria.getSize()) {
                case "small":
                    size = "icon";
                    break;
                case "medium":
                    size = "medium";
                    break;
                case "large":
                    size = "xxlarge";
                    break;
                case "extra-large":
                    size = "huge";
                    break;
            }
            searchUrl = searchUrl + "&imgsz=" + size;

        }

        if (!imageSearchCriteria.getType().equals("all")) {
            String type = "";
            switch (imageSearchCriteria.getSize()) {
                case "faces":
                    type = "face";
                    break;
                case "photos":
                    type = "photo";
                    break;
                case "clip art":
                    type = "clipart";
                    break;
                case "line art":
                    type = "lineart";
                    break;
            }
            searchUrl = searchUrl + "&imgtype=" + type;

        }

        if (!imageSearchCriteria.getColor().equals("all")) {

            searchUrl = searchUrl + "&imgcolor=" + imageSearchCriteria.getColor();
        }

        if (imageSearchCriteria.getSite() != null && !imageSearchCriteria.getSite().equals("")) {
            searchUrl = searchUrl + "&as_sitesearch=" + imageSearchCriteria.getSite();
        }
        return searchUrl;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            imageSearchCriteria = (ImageSearchCriteria) data.getSerializableExtra("searchCriteria");
            getImageSearchResults();
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_image_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ImageSearchActivity.this.query = query;
                getImageSearchResults();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
