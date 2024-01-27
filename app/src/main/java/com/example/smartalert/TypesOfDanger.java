package com.example.smartalert;

import androidx.annotation.NonNull;

public enum TypesOfDanger {
    FLOOD(MyApplication.getAppContext().getResources().getString(R.string.flood)),
    FIRE(MyApplication.getAppContext().getResources().getString(R.string.fire)),
    EARTHQUAKE(MyApplication.getAppContext().getResources().getString(R.string.earthquake)),
    HURRICANE(MyApplication.getAppContext().getResources().getString(R.string.hurricane)),
    SNOWSTORM(MyApplication.getAppContext().getResources().getString(R.string.snowstorm)),
    OTHER(MyApplication.getAppContext().getResources().getString(R.string.other));

    private final String dangerName;

    TypesOfDanger(String dangerName){
        this.dangerName = dangerName;
    }

    @NonNull
    @Override public String toString(){
        return dangerName;
    }

}
