package com.example.darwin.umnify.maps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.darwin.umnify.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampusMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap map;
    private LocationManager locationManager;
    private Spinner locationsSpinner;
    private HashMap<Marker, CampusLocation> markerLocation;
    private LocationInfoWindow locationInfoUM;

    private ImageButton toolbarBackButton;

    private static final LatLng um = new LatLng(7.066696, 125.596472);
    private static final LatLng umMatinaGate = new LatLng(7.064882, 125.598426);
    private static final LatLng umMatinaBE = new LatLng(7.065707, 125.596781);
    private static final LatLng umMatinaBEFoodcourt = new LatLng(7.065819, 125.596048);
    private static final LatLng umMatinaCafeteria = new LatLng(7.066074, 125.596228);
    private static final LatLng umMatinaECarTerminal1 = new LatLng(7.065037, 125.598129);
    private static final LatLng umMatinaHighSchool = new LatLng(7.064706, 125.596791);
    private static final LatLng umMatinaGET = new LatLng(7.067502, 125.596748);
    private static final LatLng umMatinaDPT = new LatLng(7.068237, 125.596092);
    private static final LatLng umMatinaSocialHall = new LatLng(7.068785, 125.595565);
    private static final LatLng umMatinaProfessionalSchool = new LatLng(7.067951, 125.594936);
    private static final LatLng umMatinaTrackAndField = new LatLng(7.069357, 125.594661);

    private List<CampusLocation> umLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_map);

        toolbarBackButton = (ImageButton) findViewById(R.id.back);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        locationsSpinner = (Spinner) findViewById(R.id.locations);
        umLocations = new ArrayList<>();
        markerLocation = new HashMap<>();

        locationInfoUM = new LocationInfoWindow(markerLocation, this);

        umLocations.add(new CampusLocation("University of Mindanao",
                "University Matina Campus", um, BitmapFactory.decodeResource(getResources(), R.drawable.um_logo_small),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_be)));
        umLocations.add(new CampusLocation("Main Gate",
                "UM Matina Main Gate", umMatinaGate, BitmapFactory.decodeResource(getResources(), R.drawable.gate_fence),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_be)));
        umLocations.add(new CampusLocation("BE Building",
                "Business and Education Building", umMatinaBE, BitmapFactory.decodeResource(getResources(), R.drawable.school_building),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_be)));
        umLocations.add( new CampusLocation("BE Foodcourt",
                "Foodcourt located at the back of BE Building", umMatinaBEFoodcourt, BitmapFactory.decodeResource(getResources(), R.drawable.foodcourt),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_foodcourt)));
        umLocations.add(new CampusLocation("Cafeteria",
                "University cafeteria", umMatinaCafeteria, BitmapFactory.decodeResource(getResources(), R.drawable.cafeteria),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_cafeteria)));
        umLocations.add( new CampusLocation("E-Car Terminal",
                "E-Car terminal located at the main entrance", umMatinaECarTerminal1, BitmapFactory.decodeResource(getResources(), R.drawable.bus_terminal),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_term1)));
        umLocations.add(new CampusLocation("HighSchool Department",
                "Highschool section of the university", umMatinaHighSchool, BitmapFactory.decodeResource(getResources(), R.drawable.school_building),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_highschool)));
        umLocations.add(new CampusLocation("GET Building",
                "Guillermo E Torres building", umMatinaGET, BitmapFactory.decodeResource(getResources(), R.drawable.school_building),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_get)));
        umLocations.add(new CampusLocation("DPT Building",
                "Dolores P Torres building", umMatinaDPT, BitmapFactory.decodeResource(getResources(), R.drawable.school_building),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_dpt)));
        umLocations.add(new CampusLocation("Social Hall",
                "An open hall located beside DPT", umMatinaSocialHall, BitmapFactory.decodeResource(getResources(), R.drawable.social),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_social)));
        umLocations.add(new CampusLocation("Professional School Building",
                "Graduate school", umMatinaProfessionalSchool, BitmapFactory.decodeResource(getResources(), R.drawable.school_building),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_ps)));
        umLocations.add(new CampusLocation("Track and Field",
                "Track and Field where atheletes usually practice", umMatinaTrackAndField, BitmapFactory.decodeResource(getResources(), R.drawable.track_and_field),
                BitmapFactory.decodeResource(getResources(), R.drawable.um_track_field)));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.campus_map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setBuildingsEnabled(true);

        map.setInfoWindowAdapter(locationInfoUM);
        locationsSpinner.setOnItemSelectedListener(new MapLocationListener(umLocations, map));


        for(CampusLocation location: umLocations){
            Marker marker = location.addMapMarker(map);
            markerLocation.put(marker, location);
        }

        CampusLocation location = umLocations.get(0);
        location.addMapMarker(map);
        location.showMarker();



        CameraPosition initialPosition = new CameraPosition(um, 18, 60, 0);
        CameraUpdate initialPositionUpdate = CameraUpdateFactory.newCameraPosition(initialPosition);

        map.moveCamera(initialPositionUpdate);
        map.animateCamera(initialPositionUpdate);

        //  map.setMyLocationEnabled(true);

    }

    @Override
    public void onLocationChanged(Location location) {

        map.clear();
        //LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


        //MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.position(currentLocation);
        //markerOptions.title("i'm here");

        //map.addMarker(markerOptions);

        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));

        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
