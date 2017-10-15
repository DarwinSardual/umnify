package com.example.darwin.umnify.maps;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darwin on 9/13/17.
 */

public class MapLocationListener implements AdapterView.OnItemSelectedListener {

    private List<CampusLocation> locations;
    private GoogleMap map;

    public MapLocationListener(List<CampusLocation> locations ,GoogleMap map){

        this.locations = locations;
        this.map = map;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        locations.get(i).addMapMarker(map);
        locations.get(i).showMarker();
        //map.setInfoWindowAdapter(windows.get(i));

        CameraPosition initialPosition = new CameraPosition(locations.get(i).getCoordinates(), 18, 60, 0);
        CameraUpdate initialPositionUpdate = CameraUpdateFactory.newCameraPosition(initialPosition);

        map.animateCamera(initialPositionUpdate);
        //map.moveCamera(initialPositionUpdate);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
