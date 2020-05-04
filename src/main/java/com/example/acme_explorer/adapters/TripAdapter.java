package com.example.acme_explorer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acme_explorer.Constantes;
import com.example.acme_explorer.DetailsTripActivity;
import com.example.acme_explorer.ListTripsActivity;
import com.example.acme_explorer.R;
import com.example.acme_explorer.entity.Trip;
import com.example.acme_explorer.services.FirestoreService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TripAdapter  extends
        RecyclerView.Adapter<TripAdapter.ViewHolder>  implements EventListener<QuerySnapshot> {

    private List<Trip> mTrips = new ArrayList<>();
    private DataChangedListener mDataChangedListener;
    private ItemErrorListener mErrorListener;
    private Context context;
    public final ListenerRegistration listenerRegistration;
    private boolean filtered;
    private Long filter_priceMin = Long.valueOf(0);
    private Long filter_priceMax = Long.valueOf(0);
    private Long filter_dateInit = Long.valueOf(0);
    private Long filter_dateEnd = Long.valueOf(0);


    public TripAdapter() {
        this.filtered = false;
        listenerRegistration = FirestoreService.getServiceInstance().getTrips(this);
    }

    public TripAdapter(boolean filtered, Long filter_dateInit, Long filter_dateEnd, Long filter_priceMin, Long filter_priceMax) {
        this.filtered = filtered;
        this.filter_dateInit = filter_dateInit;
        this.filter_dateEnd = filter_dateEnd;
        this.filter_priceMin = filter_priceMin;
        this.filter_priceMax = filter_priceMax;
        listenerRegistration = FirestoreService.getServiceInstance().getTrips(this);
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
        Log.i( "MasterITS", "Trip Recicler " + trip.toString() );
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

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if ( e != null) {
            mErrorListener.onItemError(e);
        }
        List<Trip> auxTripList = new ArrayList<>();
        mTrips.clear();
        if(queryDocumentSnapshots != null)
            auxTripList.addAll(queryDocumentSnapshots.toObjects(Trip.class));

        if(filtered)
            auxTripList.removeIf(n -> (!(check_dateInit(filter_dateInit,n) && check_dateEnd(filter_dateEnd,n) && check_priceMin(filter_priceMin,n) && check_priceMax(filter_priceMax,n))));
        mTrips.addAll(auxTripList);

        notifyDataSetChanged();
        mDataChangedListener.onDataChanged();
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

    private boolean check_dateInit(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter <= trip.getStartDateReturningDate().getTime()){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean check_dateEnd(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter >= trip.getEndDateReturningDate().getTime()){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean check_priceMin(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter <= trip.getPrice()){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean check_priceMax(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter >= trip.getPrice()){
                return true;
            } else {
                return false;
            }
        }
    }

    public void setErrorListener(ItemErrorListener itemErrorListener) {
        mErrorListener = itemErrorListener;
    }

    public interface ItemErrorListener {
        void onItemError(FirebaseFirestoreException error);
    }

    public void setDataChangedListener(DataChangedListener dataChangedListener) {
        mDataChangedListener = dataChangedListener;
    }

    public interface  DataChangedListener {
        void onDataChanged();
    }

}
