package com.shesha.projects.paynparkapp;

public class ParkingSpot {
    private String addressLine;
    private String addressLocality;
    private String locationName;
    private String vicinitySpot;

    public ParkingSpot() {
    }

    public ParkingSpot(String addressLine, String addressLocality, String locationName, String vicinitySpot) {
        this.addressLine = addressLine;
        this.addressLocality = addressLocality;
        this.locationName = locationName;
        this.vicinitySpot = vicinitySpot;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getAddressLocality() {
        return addressLocality;
    }

    public void setAddressLocality(String addressLocality) {
        this.addressLocality = addressLocality;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getVicinitySpot() {
        return vicinitySpot;
    }

    public void setVicinitySpot(String vicinitySpot) {
        this.vicinitySpot = vicinitySpot;
    }
}
