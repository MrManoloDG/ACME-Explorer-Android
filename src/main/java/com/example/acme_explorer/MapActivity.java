package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng location, tripLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            double lat = extras.getDouble("lat");
            double lng = extras.getDouble("lng");
            location = new LatLng(lat,lng);
            double lat_trip = extras.getDouble("lat_trip");
            double lng_trip = extras.getDouble("lng_trip");
            tripLocation = new LatLng(lat_trip, lng_trip);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(location);
        builder.include(tripLocation);
        LatLngBounds bounds = builder.build();

        googleMap.addMarker(new MarkerOptions().title("Tu ubicación").position(location));
        googleMap.addMarker(new MarkerOptions().title("Salida del viaje").position(tripLocation));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));


    }
}
