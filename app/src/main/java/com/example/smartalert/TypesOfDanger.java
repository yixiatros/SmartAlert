package com.example.smartalert;

import androidx.annotation.NonNull;

public enum TypesOfDanger {
    FLOOD("Flood"),
    FIRE("Fire"),
    EARTHQUAKE("Earthquake"),
    HURRICANE("Hurricane"),
    SNOWSTORM("Snowstorm"),
    OTHER("Other");

    private final String dangerName;

    TypesOfDanger(String dangerName){
        this.dangerName = dangerName;
    }

    @NonNull
    @Override public String toString(){
        return dangerName;
    }

}
