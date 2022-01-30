package com.example.immunizationbooking.View;

public interface Add_Child_View_Interface {
    void firstNameError(String message, boolean isFirstNameEmpty);
    void lastNameError(String message, boolean isLastNameEmpty);
    void dobError(String message, boolean isDobEmpty);
}
