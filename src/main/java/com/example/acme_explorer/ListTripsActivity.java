package com.example.acme_explorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.acme_explorer.adapters.TripAdapter;
import com.example.acme_explorer.entity.Trip;

import java.util.ArrayList;

public class ListTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trip> trips;
    LinearLayout filter;
    Switch columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trips);
        recyclerView=findViewById(R.id.recyclerViewTrips);
        trips=Trip.generateTrips();
        TripAdapter adapter = new TripAdapter(trips);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        columns = findViewById(R.id.columns);
        columns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(columns.isChecked()){
                    recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),1));
                }
            }
        });

        filter = findViewById(R.id.layoutFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Filter View saving in shared preferences
            }
        });
    }
}
