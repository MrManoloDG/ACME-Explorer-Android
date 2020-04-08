package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.acme_explorer.adapters.TripAdapter;
import com.example.acme_explorer.entity.Trip;

import java.util.ArrayList;

public class ListTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trips);
        recyclerView=findViewById(R.id.recyclerViewTrips);
        trips=Trip.generateTrips();
        TripAdapter adapter = new TripAdapter(trips);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
    }
}
