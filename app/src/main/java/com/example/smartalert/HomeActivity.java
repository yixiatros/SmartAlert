package com.example.smartalert;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.backend.DangerReviewer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private final int GALLERY_REQ_CODE = 1000;

    FirebaseDatabase database;
    DatabaseReference reference;
    LocationManager locationManager;

    private ImageView uploadImageImageView;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        database = getInstance();
        reference = database.getReference().child("nonReviewedAlerts");

        DangerReviewer dangerReviewer = new DangerReviewer();
        dangerReviewer.start(this);

        LocaleHelper.checkLocale(this);

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
        final Button uploadImageButton = dialog.findViewById(R.id.uploadImageButton);
        uploadImageImageView = dialog.findViewById(R.id.uploadImageImageView);

        List<String> typesOfDanger = new ArrayList<>();
        for (TypesOfDanger t : TypesOfDanger.values()) {
            typesOfDanger.add(t.toString(getResources()));
        }

        typeOfDanger.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdown_item, typesOfDanger));

        reportButton.setOnClickListener((v) -> {
            if (!checkPermissions())
                return;

            reference.push().setValue(
                    new Alert(
                            dangerName.getText().toString(),
                            comment.getText().toString(),
                            TypesOfDanger.values()[typeOfDanger.getSelectedItemPosition()],
                            longitude,
                            latitude
                    )
            );
            dialog.dismiss();
        });

        uploadImageButton.setOnClickListener((v) -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
    }

    // On select Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                if (data != null) {
                    uploadImageImageView.getLayoutParams().height = 300;
                    uploadImageImageView.getLayoutParams().width = 300;
                    uploadImageImageView.setImageURI(data.getData());
                    uploadImageImageView.requestLayout();
                }
            }
        }
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

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}