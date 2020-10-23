package com.shesha.projects.paynparkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shesha.projects.paynparkapp.adapters.ParkingHistoryRecyclerAdapter;

import java.util.ArrayList;

public class ParkingHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<BookingDetail> bookingDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);
        recyclerView = findViewById(R.id.parkingHistoryRecyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        bookingDetails = new ArrayList<>();
        /*
        Get parking history details from Firestore collection to display the data.
         */
        firebaseFirestore.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Parking History")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                        {
                            BookingDetail bookingDetail = new BookingDetail();
                            bookingDetail.setCost(String.valueOf(queryDocumentSnapshot.get("Cost")));
                            bookingDetail.setDuration(String.valueOf(queryDocumentSnapshot.get("Duration")));
                            bookingDetail.setLocation(String.valueOf(queryDocumentSnapshot.get("Location")));
                            bookingDetails.add(bookingDetail);
                        }
                        ParkingHistoryRecyclerAdapter parkingHistoryRecyclerAdapter = new ParkingHistoryRecyclerAdapter(bookingDetails,getApplicationContext());
                        recyclerView.setAdapter(parkingHistoryRecyclerAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ParkingHistoryActivity.this));

                    }
                });
    }
}