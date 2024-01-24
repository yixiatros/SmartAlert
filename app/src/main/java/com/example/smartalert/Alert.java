package com.example.smartalert;

public class Alert {

    private int id;
    private String title;
    private String message;
    private TypesOfDanger typeOfDanger;

    public Alert(int id, String title, String message, TypesOfDanger typeOfDanger) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.typeOfDanger = typeOfDanger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
