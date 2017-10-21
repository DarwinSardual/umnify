package com.example.darwin.umnify.maps;

import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by darwin on 9/6/17.
 */

public class CampusLocation {

    private LatLng coordinates;
    private MarkerOptions markerOptions;
    private String title;
    private String description;
    private Marker marker;
    private Bitmap icon;
    private Bitmap windowImage;

    public CampusLocation(String title, String description, LatLng coordinates, Bitmap icon, Bitmap windowImage){
        this.title = title;
        this.coordinates = coordinates;
        this.description = description;
        this.icon = icon;
        this.windowImage = windowImage;

        markerOptions = new MarkerOptions().position(coordinates).title(title).snippet(description);

        if(icon != null){
            BitmapDescriptor iconDescriptor = BitmapDescriptorFactory.fromBitmap(icon);
            markerOptions.icon(iconDescriptor);
        }

    }

    public Marker addMapMarker(GoogleMap map){
        if(marker == null)
            marker = map.addMarker(markerOptions);

        return marker;
    }

    public void removeMarker(){
        marker.remove();
    }

    public void showMarker(){
        if(marker != null){
            marker.showInfoWindow();
        }
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getWindowImage() {
        return windowImage;
    }
}
