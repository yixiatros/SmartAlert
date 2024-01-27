package com.example.smartalert;

import android.content.res.Resources;

import androidx.annotation.NonNull;

public enum TypesOfDanger {
    FLOOD(R.string.flood),
    FIRE(R.string.fire),
    EARTHQUAKE(R.string.earthquake),
    HURRICANE(R.string.hurricane),
    SNOWSTORM(R.string.snowstorm),
    OTHER(R.string.other);

    private final int dangerNameId;

    TypesOfDanger(int dangerNameId){
        this.dangerNameId = dangerNameId;
    }

    public String toString(Resources res){
        return res.getString(dangerNameId);
    }

}
