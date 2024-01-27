package com.example.smartalert;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;
import java.util.Objects;

public class LocaleHelper {

    public static void checkLocale(Activity currentActivity) {
        int local = MyApplication.getAppContext().getSharedPreferences("SettingsPref", MODE_PRIVATE).getInt("key_lang", 0);
        String localeToBe = "en";

        if (local == 1)
            localeToBe = "el";
        else if (local == 2)
            localeToBe = "es";

        if (Objects.equals(MyApplication.getAppContext().getResources().getConfiguration().locale, new Locale(localeToBe)))
            return;

        setLocale(localeToBe, currentActivity);
    }

    public static void setLocale(String lang, Activity currentActivity) {
        Locale myLocale = new Locale(lang);
        Configuration config = currentActivity.getResources().getConfiguration();
        config.locale = myLocale;
        currentActivity.getResources().updateConfiguration(
                currentActivity.getResources().getConfiguration(),
                currentActivity.getResources().getDisplayMetrics()
        );
    }
}
