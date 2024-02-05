package com.example.smartalert;

import android.media.Image;
import android.net.Uri;

public class AlertListItem {

    private String title;
    private String comment;
    private int aria;
    private String key;

    public AlertListItem(String title, String comment, int aria, String key) {
        this.title = title;
        this.comment = comment;
        this.aria = aria;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAria() {
        return aria;
    }

    public void setAria(int aria) {
        this.aria = aria;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
