package com.magarex.pickndrop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Time;
import java.util.List;

import static com.magarex.pickndrop.R.id.map;

public class DriverRouteActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private EditText edHome, edDestination;
    private ImageButton btnSearchHome, btnSearchDestination;
    private Marker markerHome;
    private Marker markerDestination;
    private DatePicker d_DatePicker;
    private TimePicker d_TimePicker;
    private double home_Lat;
    private double home_long;
    private double destination_long;
    private double destination_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        edHome = (EditText) findViewById(R.id.search_Box_Home);
        edDestination = (EditText) findViewById(R.id.search_Box_Destination);
        btnSearchHome = (ImageButton) findViewById(R.id.btnSearchHome);
        btnSearchDestination = (ImageButton) findViewById(R.id.btnSearchDestination);
        d_DatePicker = (DatePicker) findViewById(R.id.d_DatePicker);
        d_TimePicker = (TimePicker) findViewById(R.id.d_TimePicker);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        btnSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String home = edHome.getText().toString().trim();
                search_Location(home, edHome, view);
                edDestination.requestFocus();
            }
        });

        btnSearchDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = edDestination.getText().toString().trim();
                search_Location(destination, edDestination, view);
                edDestination.clearFocus();

                drawRoute();

            }
        });
    }

    private void drawRoute() {
        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl();
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(destination_lat, destination_long);
        getDirectionsData.execute(dataTransfer);
    }

    private void search_Location(String add, EditText field, View v) {
        List<Address> addressList = null;
        if (add != null || !add.equals("")) {
            Geocoder geocoder = new Geocoder(DriverRouteActivity.this);
            try {
                addressList = geocoder.getFromLocationName(add, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            String title = address.getAddressLine(0);
            field.setText(title);
            placeMarker(latlng, title, v);
        }
    }

    private void placeMarker(LatLng point, String title, View view) {
        if (view == btnSearchHome) {
            if (markerHome != null) { //if marker exists (not null or whatever)
                markerHome.setPosition(point);
            } else {
                markerHome = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(title));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        } else if (view == btnSearchDestination) {
            if (markerDestination != null) { //if marker exists (not null or whatever)
                markerDestination.setPosition(point);
            } else {
                markerDestination = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(title));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }
    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");

        home_Lat = markerHome.getPosition().latitude;
        home_long = markerHome.getPosition().longitude;
        destination_lat = markerDestination.getPosition().latitude;
        destination_long = markerDestination.getPosition().longitude;

        googleDirectionsUrl.append("origin="+home_Lat+","+home_long);
        googleDirectionsUrl.append("&destination="+destination_lat+","+destination_long);
        googleDirectionsUrl.append("&key="+"AIzaSyD_wV9vifAm3F7iMlEjrbkf3sMxInBfIo4");

        return googleDirectionsUrl.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

}
