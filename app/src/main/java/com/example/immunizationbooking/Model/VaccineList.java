package com.example.immunizationbooking.Model;

public class VaccineList {
    private String vaccine_name;
    private String childId;
    private String firstname;
    private String lastname;
    private String dob;
    private String gender;

    public VaccineList(String vaccine_name, String childId, String firstname, String lastname, String gender, String dob) {
        this.vaccine_name = vaccine_name;
        this.childId = childId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
    }

    public String getVaccine_name() {
        return this.vaccine_name;
    }

    public String getChildId() { return this.childId; }

    public String getFirstname() { return this.firstname; }

    public String getLastname() { return this.lastname; }

    public String getDob() { return this.dob; }

    public String getGender() { return this.gender; }
}
