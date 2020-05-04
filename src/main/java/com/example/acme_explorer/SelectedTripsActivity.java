package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.acme_explorer.adapters.SelectedAdapter;
import com.example.acme_explorer.adapters.TripAdapter;
import com.example.acme_explorer.entity.Trip;

import java.util.ArrayList;

public class SelectedTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trip> tripsSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trips);
        recyclerView=findViewById(R.id.recyclerViewTrips);
        SelectedAdapter adapter = new SelectedAdapter();
        adapter.setDataChangedListener(() -> {
            if (adapter.getItemCount() > 0 ) {
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        adapter.setErrorListener((error) -> {
            recyclerView.setVisibility(View.INVISIBLE);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

    }
}
