package com.example.smartalert;

public class AriaDanger {

    private String city;
    private String typesOfDanger;

    public AriaDanger(String city, String typesOfDanger) {
        this.city = city;
        this.typesOfDanger = typesOfDanger;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTypesOfDanger() {
        return typesOfDanger;
    }

    public void setTypesOfDanger(String typesOfDanger) {
        this.typesOfDanger = typesOfDanger;
    }
}
