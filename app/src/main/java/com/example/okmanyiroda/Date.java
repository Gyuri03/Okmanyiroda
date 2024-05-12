package com.example.okmanyiroda;

public class Date {
    private String date_time;
    private String day;
    private String owner;

    public Date(String date_time, String owner) {
        this.date_time = date_time;
        this.owner = owner;
    }

    public Date() {  }

    public String getDate_time() {
        return date_time;
    }

    public String _getDay() {
        return day;
    }

    public String getOwner() {
        return owner;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
