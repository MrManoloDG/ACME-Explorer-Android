package com.example.acme_explorer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acme_explorer.DetailsTripActivity;
import com.example.acme_explorer.R;
import com.example.acme_explorer.entity.Trip;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SelectedAdapter extends
        RecyclerView.Adapter<SelectedAdapter.ViewHolder> {

    private List<Trip> mTrips;
    private Context context;
    public SelectedAdapter(List<Trip> mTrips) {
        this.mTrips = mTrips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tripView = layoutInflater.inflate(R.layout.trip_selected_item, parent, false);
        return new ViewHolder(tripView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trip trip = mTrips.get(position);
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageView;
        ImageButton imageButton = holder.imageButton;

        textViewTitle.setText(trip.getTitle());
        DecimalFormat df = new DecimalFormat("#.00");
        textViewDescription.setText("Price: "+ df.format(trip.getPrice()) + "€." + trip.getDescription());
        Picasso.get()
                .load(trip.getImgUrl())
                .placeholder(R.drawable.ic_sun)
                .error(R.drawable.ic_sun)
                .resize(350, 350)
                .onlyScaleDown()
                .into(imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Trip added to shopping cart", Snackbar.LENGTH_LONG)
                        .setAction("Buy", null).show();
            }
        });


        holder.tripView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, DetailsTripActivity.class);
                intent.putExtra("trip", trip);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, textViewDescription;
        private ImageView imageView;
        private ImageButton imageButton;
        private View tripView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripView = itemView;
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageViewTrip);
            imageButton = itemView.findViewById(R.id.imageButtonSelected);
        }
    }
}
