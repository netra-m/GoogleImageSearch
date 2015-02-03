package com.yahoo.netram.googleimagesearch.activities;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.yahoo.netram.googleimagesearch.R;
import com.yahoo.netram.googleimagesearch.models.ImageSearchCriteria;

import java.util.ArrayList;
import java.util.Arrays;


public class SettingsFragment extends DialogFragment {

    private ImageSearchCriteria imageSearchCriteria;

    private Spinner sizeSpinner;
    private Spinner colorSpinner;
    private Spinner typeSpinner;
    private EditText siteEditText;

    private ArrayList<String> sizes;
    private ArrayList<String> colors;
    private ArrayList<String> types;

    private SettingsCallBack callBack;

    public SettingsFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SettingsFragment newInstance(String titleOfForm) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        callBack = (SettingsCallBack) a;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_NoActionBar_TranslucentDecor);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, null);

        final Drawable d = new ColorDrawable(Color.DKGRAY);
        d.setAlpha(200);

        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container);
        getDialog().setCanceledOnTouchOutside(true);

        sizes = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_size_array)));
        colors = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_color_array)));
        types = new ArrayList(Arrays.asList((String[]) getResources().getStringArray(R.array.image_type_array)));

        sizeSpinner = (Spinner) view.findViewById(R.id.size_spinner);
        colorSpinner = (Spinner) view.findViewById(R.id.color_spinner);
        typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
        siteEditText = (EditText) view.findViewById(R.id.etSiteFilter);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave(v);
                dismiss();
            }
        });

        imageSearchCriteria = (ImageSearchCriteria) this.getArguments().get("data");

        sizeSpinner.setSelection(sizes.indexOf(imageSearchCriteria.getSize()));

        colorSpinner.setSelection(colors.indexOf(imageSearchCriteria.getColor()));

        typeSpinner.setSelection(types.indexOf(imageSearchCriteria.getType()));

        siteEditText.setText(imageSearchCriteria.getSite());

        return view;
    }

    public void onSave(View view) {

        ImageSearchCriteria imageSearchCriteria = new ImageSearchCriteria();

        imageSearchCriteria.setSize(sizes.get(sizeSpinner.getSelectedItemPosition()));
        imageSearchCriteria.setColor(colors.get(colorSpinner.getSelectedItemPosition()));
        imageSearchCriteria.setType(types.get(typeSpinner.getSelectedItemPosition()));
        imageSearchCriteria.setSite(siteEditText.getText().toString());

        callBack.onSettingsSaved(imageSearchCriteria);

    }


}
