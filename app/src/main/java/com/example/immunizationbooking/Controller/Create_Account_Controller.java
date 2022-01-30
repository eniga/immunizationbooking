package com.example.immunizationbooking.Controller;

import com.example.immunizationbooking.Model.Create_Account_User;
import com.example.immunizationbooking.View.Create_Account_View_Interface;

public class Create_Account_Controller implements Create_Account_Controller_Interface {
    Create_Account_View_Interface create_account_view;

    public Create_Account_Controller(Create_Account_View_Interface create_account_view_interface) {
        create_account_view = create_account_view_interface;
    }

    @Override
    public boolean onSubmit(String firstname, String lastname, String phone, String post_code, String city, String address, String email, String password) {
        Create_Account_User create_account_user = new Create_Account_User(firstname, lastname, phone, post_code, city, address, email, password);

        create_account_view.firstNameError("Required", create_account_user.validateFirstName());
        create_account_view.lastNameError("Required", create_account_user.validateLastName());
        create_account_view.addressError("Required", create_account_user.validateAddress());
        create_account_view.cityError("Required", create_account_user.validateCity());
        create_account_view.phoneError("Reqired", create_account_user.validatePhone());
        create_account_view.postCodeError("Required", create_account_user.validatePostCode());
        create_account_view.emailError("Please enter a valid email", create_account_user.validateEmail());
        create_account_view.passwordError("Password should not be less than 6 characters", create_account_user.validatePassword());

        if(create_account_user.validateEmail() && create_account_user.validatePassword() && !create_account_user.validatePostCode() && !create_account_user.validateFirstName() && !create_account_user.validateLastName() && !create_account_user.validateAddress() && !create_account_user.validateCity() && !create_account_user.validatePhone()) {
            return true;
        } else {
            return false;
        }
    }
}
