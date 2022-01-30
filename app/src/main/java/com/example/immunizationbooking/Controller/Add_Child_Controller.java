package com.example.immunizationbooking.Controller;

import com.example.immunizationbooking.Model.Add_Child;
import com.example.immunizationbooking.View.Add_Child_View_Interface;

public class Add_Child_Controller implements Add_Child_Controller_Interface {
    Add_Child_View_Interface add_child_view;

    public Add_Child_Controller(Add_Child_View_Interface add_child_view_interface) {
        add_child_view = add_child_view_interface;
    }

    @Override
    public boolean onSubmit(String firstName, String lastName, String unitNo, String code, String dob, String nhsNumber, String gender) {
        Add_Child add_child = new Add_Child(firstName, lastName, unitNo, code, dob, nhsNumber, gender);

        if(add_child.validateFirstName() && add_child.validateLastName() && add_child.validateDob()) {
            return true;
        }
        return false;
    }
}
