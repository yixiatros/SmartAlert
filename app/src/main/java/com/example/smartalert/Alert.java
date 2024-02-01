package com.example.smartalert;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.Date;

public class Alert {
    private String title;
    private String message;
    private TypesOfDanger typeOfDanger;

    private double longitude;
    private double latitude;

    private long timeOfEvent;

    public Alert() {
    }

    public Alert(String title, String message, TypesOfDanger typeOfDanger, double longitude, double latitude) {
        this.title = title;
        this.message = message;
        this.typeOfDanger = typeOfDanger;
        this.longitude = longitude;
        this.latitude = latitude;

        timeOfEvent = new Date().getTime();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TypesOfDanger getTypeOfDanger() {
        return typeOfDanger;
    }

    public void setTypeOfDanger(TypesOfDanger typeOfDanger) {
        this.typeOfDanger = typeOfDanger;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public long getTimeOfEvent() {
        return timeOfEvent;
    }

    public void setTimeOfEvent(long timeOfEvent) {
        this.timeOfEvent = timeOfEvent;
    }
}
