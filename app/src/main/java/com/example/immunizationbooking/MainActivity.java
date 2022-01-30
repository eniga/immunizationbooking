package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immunizationbooking.Controller.Login_Controller;
import com.example.immunizationbooking.View.Login_View_Interface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements Login_View_Interface {
    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    Button loginButton;
    TextView create_account;
    FrameLayout progress_overlay;
    LinearLayout login_form;
    FirebaseFirestore db;

    Login_Controller login_controller;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("START", "Starting...");
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent dashboard = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(dashboard);
        }

        login_controller = new Login_Controller(this);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        create_account = (TextView) findViewById(R.id.create_account_link);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_controller.onLogin(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_account_activity = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(create_account_activity);
            }
        });
    }

    private void SignIn(String email, String password) {
        progress_overlay = (FrameLayout) findViewById(R.id.progress_overlay);
        login_form = (LinearLayout) findViewById(R.id.login_form);
        progress_overlay.setVisibility(View.VISIBLE);
        login_form.setVisibility(View.GONE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNED IN", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.i("Login Data", email.toString());
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.i("Result Data", document.getId() + " => " + document.getData());
                                            Log.i("First Name", document.getString("firstname"));

                                            // Storing data into SharedPreferences
                                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                                            // Creating an Editor object to edit(write to the file)
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                            // Storing the key and its value as the data fetched from edittext
                                            myEdit.putString("firstname", document.getString("firstname"));
                                            myEdit.putString("lastname", document.getString("lastname"));
                                            myEdit.putString("id", document.getId());

                                            // Once the changes have been made,
                                            // we need to commit to apply those changes made,
                                            // otherwise, it will throw an error
                                            myEdit.commit();

                                            Log.i("USER:", user.toString());
                                            progress_overlay.setVisibility(View.GONE);
                                            login_form.setVisibility(View.VISIBLE);
                                            Intent dashboard = new Intent(MainActivity.this, DashboardActivity.class);
                                            startActivity(dashboard);
                                        }
                                    } else {
                                        Log.i("Result Data", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    progress_overlay.setVisibility(View.GONE);
                    login_form.setVisibility(View.VISIBLE);
                    // If sign in fails, display a message to the user.
                    Log.w("FAILED", "signInWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Invalid login details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onLoginSuccess(String message, String email, String password) {
        this.SignIn(email, password);
    }

    @Override
    public void onLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}