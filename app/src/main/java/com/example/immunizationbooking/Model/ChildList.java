package com.example.immunizationbooking.Model;

public class ChildList {
    private String id;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String code;
    private String unit_no;
    private String nhs;

    public ChildList(String id, String firstName, String lastName, String dob, String gender, String code, String unit_no, String nhs) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.code = code;
        this.unit_no = unit_no;
        this.nhs = nhs;
    }

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getDob() {
        return this.dob;
    }

    public String getGender() {
        return this.gender;
    }

    public String getCode() {
        return code;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public String getNhs() {
        return nhs;
    }
}
