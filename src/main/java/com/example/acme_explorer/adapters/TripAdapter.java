package com.example.acme_explorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acme_explorer.R;
import com.example.acme_explorer.entity.Trip;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripAdapter  extends
        RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> mTrips;

    public TripAdapter(List<Trip> mTrips) {
        this.mTrips = mTrips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tripView = layoutInflater.inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(tripView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTrips.get(position);
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageView;

        textViewTitle.setText(trip.getTitle());
        textViewDescription.setText("Price: "+ trip.getPrice() + "€." + trip.getDescription());
        Picasso.get()
                .load(trip.getImgUrl())
                .placeholder(R.drawable.ic_sun)
                .error(R.drawable.ic_sun)
                .resize(100, 100)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, textViewDescription;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageViewTrip);
        }
    }
}
