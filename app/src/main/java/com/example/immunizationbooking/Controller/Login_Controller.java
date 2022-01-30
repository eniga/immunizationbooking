package com.example.immunizationbooking.Controller;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.immunizationbooking.Model.Login_User;
import com.example.immunizationbooking.View.Login_View_Interface;

public class Login_Controller implements Login_Controller_Interface {
    Login_View_Interface Login_View;

    public Login_Controller(Login_View_Interface login_View) {
        Login_View = login_View;
    }

    @Override
    public void onLogin(String email, String password) {
        Login_User login_user = new Login_User(email, password);
        int loginstate = login_user.isValid();

        if(loginstate == 0) {
            Login_View.onLoginError("Please your email address");
        } else if(loginstate == 1) {
            Login_View.onLoginError("Please enter a valid email address");
        } else {
            Login_View.onLoginSuccess("Login was successful", email, password);
        }
    }
}
