package com.example.immunizationbooking.Model;

public interface Add_Child_Model_Interface {
    String getFirstName();
    String getLastName();
    String getUnitNo();
    String getCode();
    String getDob();
    String getNhsNumber();
    String getGender();

    boolean validateFirstName();
    boolean validateLastName();
    boolean validateDob();
}
