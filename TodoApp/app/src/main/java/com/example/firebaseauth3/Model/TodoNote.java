package com.example.firebaseauth3.Model;

import java.util.Date;

public class TodoNote {

    private String title;
    private String date;
    private int status;

    public TodoNote() {
    }

    public TodoNote(String title, String date, int status) {
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
