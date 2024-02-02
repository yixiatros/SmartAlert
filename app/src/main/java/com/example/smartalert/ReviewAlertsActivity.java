package com.example.smartalert;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backend.DangerReviewer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReviewAlertsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    private List<Double> longitudeList = new ArrayList<>();
    private List<Double> latitudeList = new ArrayList<>();
    private List<String> typeOfDangerList = new ArrayList<>();
    private List<LocalDateTime> timeOfEventList = new ArrayList<>();

    private RecyclerView ariaRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_alerts);

        ariaRecyclerView = findViewById(R.id.ariaRecyclerView);

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

                ArrayList<AriaDanger> ariaDangerList = new ArrayList<>();
                for (List<Integer> integerList : ariaGroupList) {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latitudeList.get(integerList.get(0)), longitudeList.get(integerList.get(0)), 1);

                        if (addresses.size() > 0) {
                            AriaDanger ariaDanger = new AriaDanger(addresses.get(0).getLocality(), typeOfDangerList.get(integerList.get(0)));
                            ariaDangerList.add(ariaDanger);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                ariaRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ariaRecyclerView.setAdapter(new AriaRecyclerViewAdapter(getApplicationContext(), ariaDangerList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}