package com.shesha.projects.paynparkapp;

public class BookingDetail {
    private String cost;
    private String duration;
    private String location;

    public BookingDetail() {
    }

    public BookingDetail(String cost, String duration, String location) {
        this.cost = cost;
        this.duration = duration;
        this.location = location;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
