package com.example.backend;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangerReviewer {

    Context context;

    FirebaseDatabase database;
    DatabaseReference reference;

    public void start(Context context) {
        this.context = context;

        database = getInstance();
        reference = database.getReference().child("nonReviewedAlerts");
    }

    private void onDataChange() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
