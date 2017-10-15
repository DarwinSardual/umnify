package com.example.darwin.umnify.maps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by darwin on 9/14/17.
 */

public class LocationInfoWindow implements GoogleMap.InfoWindowAdapter {


    private HashMap<Marker, CampusLocation> markerLocations;
    private Activity activity;

    private final View window;
    private TextView titleView;
    private TextView descriptionView;
    private ImageView imageView;

    public LocationInfoWindow(HashMap<Marker, CampusLocation> markerLocations, Activity activity){
        this.activity = activity;
        this.markerLocations = markerLocations;
        window = activity.getLayoutInflater().inflate(R.layout.location_info, null);

        titleView = (TextView) window.findViewById(R.id.title);
        descriptionView = (TextView) window.findViewById(R.id.location_info_description);
        imageView = (ImageView) window.findViewById(R.id.image);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        CampusLocation location = markerLocations.get(marker);

        titleView.setText(location.getTitle());
        descriptionView.setText(location.getDescription());
        imageView.setImageBitmap(location.getWindowImage());

        return window;
    }
}
