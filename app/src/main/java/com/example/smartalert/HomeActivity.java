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
import android.widget.Toast;

import com.example.backend.DangerReviewer;
import com.example.smartalert.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private final int GALLERY_REQ_CODE = 1000;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    LocationManager locationManager;

    private ImageView uploadImageImageView;
    private Button reportEventButton;

    private double latitude;
    private double longitude;

    private Intent imageData;

    private boolean isCPO = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        database = getInstance();
        databaseReference = database.getReference().child("nonReviewedAlerts");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reportEventButton = findViewById(R.id.reportEventButton);

        checkIfCPO();

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

            DatabaseReference keyReference = databaseReference.push();
            String key = keyReference.getKey();
            keyReference.setValue(
                    new Alert(
                            dangerName.getText().toString(),
                            comment.getText().toString(),
                            TypesOfDanger.values()[typeOfDanger.getSelectedItemPosition()],
                            longitude,
                            latitude
                    )
            );

            keyReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(HomeActivity.this, R.string.successful_report, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            uploadImage(key);

            dialog.dismiss();
        });

        uploadImageButton.setOnClickListener((v) -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
    }

    private void uploadImage(String key) {
        if (imageData == null)
            return;

        UploadTask uploadTask = storageReference.child(key).putFile(Objects.requireNonNull(imageData.getData()));

        uploadTask.addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, R.string.image_uploaded_successfully, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception  -> {
             Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                    imageData = data;
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

    public void onLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void doIfCPO() {
        if (isCPO){
            reportEventButton.setText("Hello");
            reportEventButton.setActivated(false);
            reportEventButton.setVisibility(View.GONE);
        }
    }

    private void checkIfCPO() {
        Query query = database.getReference().child("users").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    for (DataSnapshot i : item.getChildren()){
                        if (Objects.equals(i.getKey(), "cpo")){
                            isCPO = Boolean.parseBoolean(i.getValue().toString());
                            doIfCPO();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}