package com.example.immunizationbooking.Model;

import android.text.TextUtils;

public class Add_Child implements Add_Child_Model_Interface {
    private String firstName;
    private String lastName;
    private String unitNo;
    private String code;
    private String dob;
    private String nhsNumber;
    private String gender;

    public Add_Child(String firstName, String lastName, String unitNo, String code, String dob, String nhsNumber, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.unitNo = unitNo;
        this.code = code;
        this.dob = dob;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String getUnitNo() {
        return unitNo;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDob() {
        return this.dob;
    }

    @Override
    public String getNhsNumber() {
        return this.nhsNumber;
    }

    @Override
    public String getGender() {
        return this.gender;
    }

    @Override
    public boolean validateFirstName() {
        return !TextUtils.isEmpty(this.getFirstName());
    }

    @Override
    public boolean validateLastName() {
        return !TextUtils.isEmpty(this.getLastName());
    }

    @Override
    public boolean validateDob() {
        return !TextUtils.isEmpty(this.getDob());
    }
}
