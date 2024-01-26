package com.example.smartalert;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartalert.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    FirebaseDatabase database;
    DatabaseReference reference;
    LocationManager locationManager;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        database = getInstance();
        reference = database.getReference().child("nonReviewedAlerts");

        checkPermissions();
    }

    public void onReportEvent(View view) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.report_event_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
        final EditText dangerName = dialog.findViewById(R.id.dangerNameEditText);
        final EditText comment = dialog.findViewById(R.id.commentEditText);
        final Spinner typeOfDanger = dialog.findViewById(R.id.typeOfDangerSpinner);
        final Button reportButton = dialog.findViewById(R.id.reportButton);

        typeOfDanger.setAdapter(new ArrayAdapter<TypesOfDanger>(this, R.layout.dropdown_item, TypesOfDanger.values()));

        reportButton.setOnClickListener((v) -> {
            if (!checkPermissions())
                return;

            reference.push().setValue(
                    new Alert(
                            dangerName.getText().toString(),
                            comment.getText().toString(),
                            TypesOfDanger.valueOf(typeOfDanger.getSelectedItem().toString().toUpperCase()),
                            longitude,
                            latitude
                    )
            );
            dialog.dismiss();
        });
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        return true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLatitude();
        latitude = location.getLatitude();
    }
}