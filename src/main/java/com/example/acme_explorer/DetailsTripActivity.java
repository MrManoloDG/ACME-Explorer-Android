package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acme_explorer.entity.Trip;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DetailsTripActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewPrice, textViewStartDate, textViewEndDate, textViewDescription;
    private ImageView imageView;
    private ImageButton imageButton;
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
        Trip trip = (Trip) intent.getSerializableExtra("trip");
        textViewTitle.setText(trip.getTitle());
        DecimalFormat dfp = new DecimalFormat("#.00");
        textViewPrice.setText(dfp.format(trip.getPrice()) + "€");
        // Format to Date String
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        textViewStartDate.setText(df.format(trip.getStartDate()));
        textViewEndDate.setText(df.format(trip.getEndDate()));

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
                Constantes.viajes.get(trip.getId()).setSelected(!trip.isSelected());
                trip.setSelected(!trip.isSelected());
                if (trip.isSelected()){
                    imageButton.setImageResource(R.drawable.ic_star_black_24dp);
                } else {
                    imageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }
}
