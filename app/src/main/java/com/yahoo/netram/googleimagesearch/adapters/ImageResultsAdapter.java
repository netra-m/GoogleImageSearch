package com.yahoo.netram.googleimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.netram.googleimagesearch.R;
import com.yahoo.netram.googleimagesearch.models.ImageResult;

import java.util.List;

/**
 * Created by netram on 2/1/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapter(Context context, List<ImageResult> imageResults) {
        super(context, R.layout.item_image_result, imageResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        //clear out image from last time
        ivImage.setImageResource(0);
        tvTitle.setText(Html.fromHtml(imageResult.getTitle()));

        Picasso.with(getContext()).load(imageResult.getThumbNailUrl()).into(ivImage);

        return convertView;
    }
}
