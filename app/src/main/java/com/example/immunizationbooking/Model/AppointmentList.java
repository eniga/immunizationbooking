package com.example.immunizationbooking.Model;

public class AppointmentList {
    private String immunizer;
    private String vaccine;
    private String venue;
    private String child_name;
    private String vaccine_date;

    public AppointmentList(String immunizer, String vaccine_date, String vaccine, String venue, String child_name) {
        this.immunizer = immunizer;
        this.vaccine = vaccine;
        this.venue = venue;
        this.child_name = child_name;
        this.vaccine_date = vaccine_date;
    }

    public String getImmunizer() {
        return this.immunizer;
    }

    public String getVaccine() {
        return this.vaccine;
    }

    public String getVenue() {
        return this.venue;
    }

    public String getChild_name() {
        return this.child_name;
    }

    public String getVaccine_date() {
        return this.vaccine_date;
    }
}
