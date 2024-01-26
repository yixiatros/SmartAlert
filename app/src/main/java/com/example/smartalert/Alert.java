package com.example.smartalert;

public class Alert {
    private String title;
    private String message;
    private TypesOfDanger typeOfDanger;

    private double longitude;
    private double latitude;

    public Alert(String title, String message, TypesOfDanger typeOfDanger, double longitude, double latitude) {
        this.title = title;
        this.message = message;
        this.typeOfDanger = typeOfDanger;
        this.longitude = longitude;
        this.latitude = latitude;
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
}
