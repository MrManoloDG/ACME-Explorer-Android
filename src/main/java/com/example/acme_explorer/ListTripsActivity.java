package com.example.acme_explorer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.acme_explorer.adapters.TripAdapter;
import com.example.acme_explorer.entity.Trip;

import java.util.List;

public class ListTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayout filter;
    Switch columns;

    TripAdapter adapter;

    Long filter_priceMin = Long.valueOf(0);
    Long filter_priceMax = Long.valueOf(0);
    Long filter_dateInit = Long.valueOf(0);
    Long filter_dateEnd = Long.valueOf(0);
    static final int PICK_FILTER_LIST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trips);
        recyclerView=findViewById(R.id.recyclerViewTrips);
        adapter = new TripAdapter();
        confAdapter();
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
                Intent intent = new Intent(ListTripsActivity.this, FilterActivity.class);
                intent.putExtra("filter_minPrice",String.valueOf(filter_priceMin));
                intent.putExtra("filter_maxPrice",String.valueOf(filter_priceMax));
                intent.putExtra("filter_startDate",String.valueOf(filter_dateInit));
                intent.putExtra("filter_endDate",String.valueOf(filter_dateEnd));
                startActivityForResult(intent, 1);
            }
        });
    }

    private void confAdapter() {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.listenerRegistration != null) {
            adapter.listenerRegistration.remove();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILTER_LIST) {
            if (resultCode == RESULT_OK) {
                filter_priceMin = data.getLongExtra("filter_minPrice",0);
                filter_priceMax = data.getLongExtra("filter_maxPrice",0);
                filter_dateInit = data.getLongExtra("filter_startDate",0);
                filter_dateEnd = data.getLongExtra("filter_endDate",0);
                this.refreshRecycle(filter_dateInit,filter_dateEnd,filter_priceMin,filter_priceMax);
            }
        } else {
            if(requestCode == 2) {
                this.refreshRecycle(filter_dateInit,filter_dateEnd,filter_priceMin,filter_priceMax);
            }
        }
    }

    private void refreshRecycle(final Long filter_dateInit, final Long filter_dateEnd, final Long filter_priceMin, final Long filter_priceMax) {

        adapter = new TripAdapter(true,filter_dateInit,filter_dateEnd,filter_priceMin,filter_priceMax);
        confAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),1));
    }

}
