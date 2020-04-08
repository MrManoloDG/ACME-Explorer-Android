package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acme_explorer.entity.Trip;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetailsTripActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewPrice, textViewStartDate, textViewEndDate, textViewDescription;
    private ImageView imageView;
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

        // Recovering data from intent extra
        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra("trip");
        textViewTitle.setText(trip.getTitle());
        textViewPrice.setText(Float.toString(trip.getPrice()));
        // Format to Date String
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        textViewStartDate.setText(df.format(trip.getStartDate()));
        textViewEndDate.setText(df.format(trip.getEndDate()));

        textViewDescription.setText(trip.getDescription());

        Picasso.get()
                .load(trip.getImgUrl())
                .placeholder(R.drawable.ic_sun)
                .error(R.drawable.ic_sun)
                .resize(1000, 1000)
                .onlyScaleDown()
                .into(imageView);
    }
}
