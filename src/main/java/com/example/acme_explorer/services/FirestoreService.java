package com.example.acme_explorer.services;

import com.example.acme_explorer.entity.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreService {
    private static String userId;
    private static FirebaseFirestore mDatabase;
    private static FirestoreService service;

    public static FirestoreService getServiceInstance(){
        if (service == null || mDatabase == null){
            mDatabase = FirebaseFirestore.getInstance();
            service = new FirestoreService();
        }

        if ( userId == null || userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }
        return service;
    }

    public void saveTrip(Trip trip, DatabaseReference.CompletionListener listener) {
        mDatabase.collection("users").document(userId).collection("trip").add(trip).addOnCompleteListener((OnCompleteListener<DocumentReference>) listener);
    }

    public void selectTrip(Trip trip, OnCompleteListener listener) {
        Map<String,Object> selectedMap = new HashMap<>();
        selectedMap.put("selected",trip.isSelected());
        mDatabase.collection("users").document(userId).collection("trip").document(trip.getDocumentId()).update(selectedMap).addOnCompleteListener(listener);
    }

    public void getTrips(OnCompleteListener<QuerySnapshot> querySnapshotOnCompleteListener) {
        mDatabase.collection("users").document(userId).collection("trip").get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }


    public void getTripsFiltered(OnCompleteListener<QuerySnapshot> querySnapshotOnCompleteListener) {
        Task<QuerySnapshot> querySnapshotTask = mDatabase.collection("users").document(userId).collection("trip").whereGreaterThanOrEqualTo("price", 3900).limit(10).get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public void getTrip(String id, EventListener<DocumentSnapshot> snapshotListener){
        mDatabase.collection("users").document(userId).collection("trip").document(id).addSnapshotListener(snapshotListener);
    }

    public ListenerRegistration getTrips(EventListener<QuerySnapshot> querySnapshotOnCompleteListener) {
        return mDatabase.collection("users").document(userId).collection("trip").addSnapshotListener(querySnapshotOnCompleteListener);
    }

    public ListenerRegistration getTripsSelecteds(EventListener<QuerySnapshot> querySnapshotOnCompleteListener) {
        return mDatabase.collection("users").document(userId).collection("trip").whereEqualTo("selected", true).addSnapshotListener(querySnapshotOnCompleteListener);
    }

}
