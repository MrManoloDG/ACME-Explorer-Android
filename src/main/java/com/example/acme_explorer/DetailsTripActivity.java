package com.example.acme_explorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acme_explorer.entity.Trip;
import com.example.acme_explorer.services.FirestoreService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DetailsTripActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewPrice, textViewStartDate, textViewEndDate, textViewDescription;
    private ImageView imageView;
    private ImageButton imageButton;
    private TextView locationText;
    private Trip trip;
    private Location locationTrip;

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 0x125;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trip);

        imageView = findViewById(R.id.imageViewTripDetail);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        textViewDescription = findViewById(R.id.textViewDescription);
        imageButton = findViewById(R.id.imageButtonSelected);

        // Recovering data from intent extra
        Intent intent = getIntent();
        trip = (Trip) intent.getSerializableExtra("trip");
        locationTrip = new Location("");
        locationTrip.setLatitude(trip.getLatitude());
        locationTrip.setLongitude(trip.getLongitude());

        textViewTitle.setText(trip.getTitle());
        DecimalFormat dfp = new DecimalFormat("#.00");
        textViewPrice.setText(dfp.format(trip.getPrice()) + "€");
        // Format to Date String
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        textViewStartDate.setText(df.format(trip.getStartDateReturningDate()));
        textViewEndDate.setText(df.format(trip.getEndDateReturningDate()));

        textViewDescription.setText(trip.getDescription());

        if (trip.isSelected()){
            imageButton.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            imageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        Picasso.get()
                .load(trip.getImgUrl())
                .placeholder(R.drawable.ic_sun)
                .error(R.drawable.ic_sun)
                .resize(1000, 1000)
                .onlyScaleDown()
                .into(imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.setSelected(!trip.isSelected());
                FirestoreService.getServiceInstance().selectTrip(trip, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (trip.isSelected()){
                            imageButton.setImageResource(R.drawable.ic_star_black_24dp);
                        } else {
                            imageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                        }
                    }
                });
            }
        });

        locationText = findViewById(R.id.location_textView);

        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Snackbar.make(locationText, R.string.location_rationale, Snackbar.LENGTH_LONG).setAction(R.string.location_rationale_ok, view -> {
                    ActivityCompat.requestPermissions(DetailsTripActivity.this, permissions, PERMISSION_REQUEST_CODE_LOCATION );
                }).show();
            } else {
                ActivityCompat.requestPermissions(DetailsTripActivity.this, permissions, PERMISSION_REQUEST_CODE_LOCATION );
            }
        } else {
            startLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, R.string.location_cancel, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationService() {
        FusedLocationProviderClient locationServices = LocationServices.getFusedLocationProviderClient(this);

        /**
         * locationServices.getLastLocation().addOnCompleteListener(task -> {
         *                     if (task.isSuccessful() && task.getResult() != null ) {
         *                         Location location = task.getResult();
         *                         Log.i("MasterITS", "Location: " + location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAccuracy());
         *                     }
         *                 });
         */

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // locationRequest.setSmallestDisplacement(10); // Distancia minima de refresco 10 metros de desplazarse
        locationServices.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(locationResult == null || locationResult.getLastLocation() == null || !locationResult.getLastLocation().hasAccuracy()) {
                return;
            } else {
                Location location = locationResult.getLastLocation();
                locationText.setText("Distance: " + location.distanceTo(locationTrip) + " m");
                Log.i("MasterITS", "Location: " + location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAccuracy());
            }
        }
    };


    public void stopService() {
        LocationServices.getFusedLocationProviderClient(this ).removeLocationUpdates(locationCallback);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }
}
