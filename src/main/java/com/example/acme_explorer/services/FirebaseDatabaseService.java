package com.example.acme_explorer.services;

import com.example.acme_explorer.entity.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseService {

    private static String userId;
    private static FirebaseDatabaseService service;
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabaseService getServiceInstance() {
        if (service == null || mDatabase == null){
            service = new FirebaseDatabaseService();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        if ( userId == null || userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }
        return service;
    }

    public DatabaseReference getTravel(String travelId) {
       return mDatabase.getReference("user/" + userId + "/travel/" + travelId).getRef();
    }

    public DatabaseReference getTravelPrice(String travelId) {
        return mDatabase.getReference("user/" + userId + "/travel/" + travelId + "/price").getRef();
    }

    public DatabaseReference getTravel() {
        return mDatabase.getReference("user/" + userId + "/travel").getRef();
    }

    public void saveTrip(Trip trip, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("user/" + userId + "/travel").push().setValue(trip, completionListener);
    }
}
