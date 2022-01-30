package com.example.immunizationbooking.Model;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.immunizationbooking.Controller.Create_Account_Controller_Interface;

import org.w3c.dom.Text;

public class Create_Account_User implements Create_Account_Model_Interface {
    private String firstName;
    private String lastName;
    private String phone;
    private String postCode;
    private String city;
    private String address;
    private String email;
    private String password;

    public Create_Account_User(String firstName, String lastName, String phone, String postCode, String city, String address, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.postCode = postCode;
        this.city = city;
        this.address = address;
        this.email = email;
        this.password = password;
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
    public String getPhone() {
        return this.phone;
    }

    @Override
    public String getPostCode() {
        return this.postCode;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean validateFirstName() {
        return TextUtils.isEmpty(this.getFirstName());
    }

    @Override
    public boolean validateLastName() {
        return TextUtils.isEmpty(this.getLastName());
    }

    @Override
    public boolean validatePhone() {
        return TextUtils.isEmpty(this.getPhone());
    }

    @Override
    public boolean validatePostCode() {
        return TextUtils.isEmpty(this.getPostCode());
    }

    @Override
    public boolean validateCity() {
        return TextUtils.isEmpty(this.getCity());
    }

    @Override
    public boolean validateAddress() {
        return TextUtils.isEmpty(this.getAddress());
    }

    @Override
    public boolean validateEmail() {
        return !TextUtils.isEmpty(this.getEmail()) && Patterns.EMAIL_ADDRESS.matcher(this.getEmail()).matches();
    }

    @Override
    public boolean validatePassword() {
        return !TextUtils.isEmpty(this.getPassword());
    }
}
