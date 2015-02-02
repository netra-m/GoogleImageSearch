package com.yahoo.netram.googleimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.yahoo.netram.googleimagesearch.R;
import com.yahoo.netram.googleimagesearch.models.ImageSearchCriteria;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchSettingsActivity extends ActionBarActivity {

    private Spinner sizeSpinner;
    private Spinner colorSpinner;
    private Spinner typeSpinner;
    private EditText siteEditText;

    private ArrayList<String> sizes;
    private ArrayList<String> colors;
    private ArrayList<String> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_settings);

        sizes = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_size_array)));
        colors = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_color_array)));
        types = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_type_array)));

        ImageSearchCriteria imageSearchCriteria = (ImageSearchCriteria) getIntent().getSerializableExtra("searchCriteria");

        sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
        colorSpinner = (Spinner) findViewById(R.id.color_spinner);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        siteEditText = (EditText) findViewById(R.id.etSiteFilter);


        sizeSpinner.setSelection(sizes.indexOf(imageSearchCriteria.getSize()));

        colorSpinner.setSelection(colors.indexOf(imageSearchCriteria.getColor()));

        typeSpinner.setSelection(types.indexOf(imageSearchCriteria.getType()));

        siteEditText.setText(imageSearchCriteria.getSite());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveSettings(View view) {

        ImageSearchCriteria imageSearchCriteria = new ImageSearchCriteria();

        imageSearchCriteria.setSize(sizes.get(sizeSpinner.getSelectedItemPosition()));
        imageSearchCriteria.setColor(colors.get(colorSpinner.getSelectedItemPosition()));
        imageSearchCriteria.setType(types.get(typeSpinner.getSelectedItemPosition()));
        imageSearchCriteria.setSite(siteEditText.getText().toString());

        Intent data = new Intent();
        data.putExtra("searchCriteria", imageSearchCriteria);
        setResult(RESULT_OK, data);

        finish();

    }
}
