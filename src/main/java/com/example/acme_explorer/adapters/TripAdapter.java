package com.example.acme_explorer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acme_explorer.DetailsTripActivity;
import com.example.acme_explorer.ListTripsActivity;
import com.example.acme_explorer.R;
import com.example.acme_explorer.entity.Trip;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class TripAdapter  extends
        RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> mTrips;
    private Context context;
    public TripAdapter(List<Trip> mTrips) {
        this.mTrips = mTrips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tripView = layoutInflater.inflate(R.layout.trip_item, parent, false);
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


        if (trip.isSelected()){
            imageButton.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            imageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.setSelected(!trip.isSelected());
                if (trip.isSelected()){
                    imageButton.setImageResource(R.drawable.ic_star_black_24dp);
                } else {
                    imageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });


        holder.tripView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, DetailsTripActivity.class);
                intent.putExtra("trip", trip);
                ((Activity)context).startActivityForResult(intent,2);
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
