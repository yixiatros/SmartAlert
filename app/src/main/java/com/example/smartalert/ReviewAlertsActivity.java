package com.example.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.example.backend.DangerReviewer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.TimeZone;

public class ReviewAlertsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    private List<Double> longitudeList = new ArrayList<>();
    private List<Double> latitudeList = new ArrayList<>();
    private List<String> typeOfDangerList = new ArrayList<>();
    private List<LocalDateTime> timeOfEventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_alerts);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("nonReviewedAlerts");

        getAlertsFromDB();
    }

    private void getAlertsFromDB() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Alert alert = item.getValue(Alert.class);
                    assert alert!=null;
                    longitudeList.add(alert.getLongitude());
                    latitudeList.add(alert.getLatitude());
                    typeOfDangerList.add(alert.getTypeOfDanger().toString());
                    timeOfEventList.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(alert.getTimeOfEvent()), TimeZone.getDefault().toZoneId()));
                }

                DangerReviewer dangerReviewer = new DangerReviewer();
                List<List<Integer>> ariaGroupList = dangerReviewer.start(longitudeList, latitudeList, typeOfDangerList, timeOfEventList);

                for (int i = 0; i < ariaGroupList.size(); i++) {
                    Log.d("TempTest", "size of " + i + ": " + ariaGroupList.get(i).size() + " whole list: " + ariaGroupList.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}