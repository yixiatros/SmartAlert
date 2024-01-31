package com.example.smartalert.users;

public class User {

    private String uid;
    private String username;
    private String email;
    private boolean isCPO;

    public User() {

    }

    public User(String uid, boolean isCPO, String email, String username) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.isCPO = isCPO;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCPO() {
        return isCPO;
    }

    public void setCPO(boolean CPO) {
        isCPO = CPO;
    }
}
