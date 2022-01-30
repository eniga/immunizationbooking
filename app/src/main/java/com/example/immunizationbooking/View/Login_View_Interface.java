package com.example.immunizationbooking.View;

public interface Login_View_Interface {
    void onLoginSuccess(String message, String email, String password);
    void onLoginError(String message);
}
