package com.example.immunizationbooking.View;

public interface Create_Account_View_Interface {
    void firstNameError(String message, boolean isFirstNameEmpty);
    void lastNameError(String message, boolean isLastNameEmpty);
    void phoneError(String message, boolean isPhoneEmpty);
    void postCodeError(String message, boolean isPostCodeEmpty);
    void cityError(String message, boolean isCityEmpty);
    void addressError(String message, boolean isAddressEmpty);
    void emailError(String message, boolean isEmailValid);
    void passwordError(String message, boolean isPasswordEmpty);
}
