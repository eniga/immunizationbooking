package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immunizationbooking.Controller.Create_Account_Controller;
import com.example.immunizationbooking.Model.Create_Account_User;
import com.example.immunizationbooking.View.Create_Account_View_Interface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements Create_Account_View_Interface {
    private FirebaseAuth mAuth;
    TextView login_link;
    EditText firstname;
    EditText lastname;
    EditText phone;
    EditText postcode;
    EditText city;
    EditText address;
    EditText email;
    EditText password;
    TextView firstname_error;
    TextView lastname_error;
    TextView phone_error;
    TextView postcode_error;
    TextView city_error;
    TextView address_error;
    TextView email_error;
    TextView password_error;
    Button create_account;
    boolean formValidation;
    LinearLayout signup_form;
    FrameLayout progress_overlay;

    FirebaseFirestore db;

    Create_Account_Controller create_account_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        login_link = (TextView) findViewById(R.id.login_link);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        phone = (EditText) findViewById(R.id.phone);
        postcode = (EditText) findViewById(R.id.postcode);
        city = (EditText) findViewById(R.id.city);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        create_account = (Button) findViewById(R.id.create_account);

        firstname_error = (TextView) findViewById(R.id.firstname_error);
        lastname_error = (TextView) findViewById(R.id.lastname_error);
        phone_error = (TextView) findViewById(R.id.phone_error);
        postcode_error = (TextView) findViewById(R.id.postcode_error);
        city_error = (TextView) findViewById(R.id.city_error);
        address_error = (TextView) findViewById(R.id.address_error);
        email_error = (TextView) findViewById(R.id.email_error);
        password_error = (TextView) findViewById(R.id.password_error);

        create_account_controller = new Create_Account_Controller(this);

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(login);
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formValidation = create_account_controller.onSubmit(firstname.getText().toString().trim(), lastname.getText().toString().trim(), phone.getText().toString().trim(), postcode.getText().toString().trim(), city.getText().toString().trim(), address.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim());
                if(formValidation) {
                    signUp(email.getText().toString().trim(), password.getText().toString().trim(), firstname.getText().toString().trim(), lastname.getText().toString().trim(), phone.getText().toString().trim(), postcode.getText().toString().trim(), city.getText().toString().trim(), address.getText().toString().trim());
                }
            }
        });
    }

    private void signUp(String email, String password, String firstname, String lastname, String phone, String post_code, String city, String address) {
        progress_overlay = (FrameLayout) findViewById(R.id.progress_overlay);
        signup_form = (LinearLayout) findViewById(R.id.signup_form);
        signup_form.setVisibility(View.GONE);
        progress_overlay.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signup_form.setVisibility(View.VISIBLE);
                            progress_overlay.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign Up", "createUserWithEmail:success");

                            Map<String, Object> new_user = new HashMap<>();
                            new_user.put("firstname", firstname);
                            new_user.put("lastname", lastname);
                            new_user.put("phone", phone);
                            new_user.put("post_code", post_code);
                            new_user.put("city", city);
                            new_user.put("address", address);
                            new_user.put("email", email);

                            db.collection("users")
                                    .add(new_user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("Data Saved", "DocumentSnapshot written with ID: " + documentReference.getId());
                                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                                            // Creating an Editor object to edit(write to the file)
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                            // Storing the key and its value as the data fetched from edittext
                                            myEdit.putString("firstname", firstname);
                                            myEdit.putString("lastname", lastname);
                                            myEdit.putString("id", documentReference.getId());

                                            // Once the changes have been made,
                                            // we need to commit to apply those changes made,
                                            // otherwise, it will throw an error
                                            myEdit.commit();

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent dashboard = new Intent(SignupActivity.this, DashboardActivity.class);
                                            startActivity(dashboard);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Data Not Saved", "Error adding document", e);
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            signup_form.setVisibility(View.VISIBLE);
                            progress_overlay.setVisibility(View.GONE);
                            Log.w("Sign Up Error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed. Please make sure email has not been used.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    @Override
    public void firstNameError(String message, boolean isFirstNameEmpty) {
        if(isFirstNameEmpty) {
            firstname_error.setVisibility(View.VISIBLE);
        } else {
            firstname_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void lastNameError(String message, boolean isLastNameEmpty) {
        if(isLastNameEmpty) {
            lastname_error.setVisibility(View.VISIBLE);
        } else {
            lastname_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void phoneError(String message, boolean isPhoneEmpty) {
        if(isPhoneEmpty) {
            phone_error.setVisibility(View.VISIBLE);
        } else {
            phone_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void postCodeError(String message, boolean isPostCodeEmpty) {
        if(isPostCodeEmpty) {
            postcode_error.setVisibility(View.VISIBLE);
        } else {
            postcode_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void cityError(String message, boolean isCityEmpty) {
        if(isCityEmpty) {
            city_error.setVisibility(View.VISIBLE);
        } else {
            city_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void addressError(String message, boolean isAddressEmpty) {
        if(isAddressEmpty) {
            address_error.setVisibility(View.VISIBLE);
        } else {
            address_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void emailError(String message, boolean isEmailValid) {
        if(!isEmailValid) {
            email_error.setVisibility(View.VISIBLE);
        } else {
            email_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void passwordError(String message, boolean isPasswordEmpty) {
        if(!isPasswordEmpty) {
            password_error.setVisibility(View.VISIBLE);
        } else {
            password_error.setVisibility(View.GONE);
        }
    }
}