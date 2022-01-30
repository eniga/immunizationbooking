package com.example.immunizationbooking.Model;

public interface Create_Account_Model_Interface {
    String getFirstName();
    String getLastName();
    String getPhone();
    String getPostCode();
    String getCity();
    String getAddress();
    String getEmail();
    String getPassword();

    boolean validateFirstName();
    boolean validateLastName();
    boolean validatePhone();
    boolean validatePostCode();
    boolean validateCity();
    boolean validateAddress();
    boolean validateEmail();
    boolean validatePassword();
}
