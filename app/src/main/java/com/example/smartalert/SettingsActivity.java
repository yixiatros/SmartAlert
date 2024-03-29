package com.example.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.smartalert.HomeActivity;
import com.example.smartalert.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private Spinner languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences("SettingsPref", MODE_PRIVATE);

        languages = findViewById(R.id.languagesSpinner);

        List<String> lang = new ArrayList<>();
        lang.add("English");
        lang.add("Ελληνικά");
        lang.add("Español");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>((Context) this, R.layout.dropdown_item, lang);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        languages.setAdapter(adapter);

        languages.setSelection(sharedPreferences.getInt("key_lang", 0));
    }

    public void onSaveChangesClick(View view) {
        sharedPreferences
                .edit()
                .putInt("key_lang", languages.getSelectedItemPosition())
                .apply();

        LocaleHelper.checkLocale(this);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}