package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immunizationbooking.Controller.Add_Child_Controller;
import com.example.immunizationbooking.View.Add_Child_View_Interface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddChildActivity extends AppCompatActivity implements Add_Child_View_Interface {
    TextView username;
    ImageView menu;
    ImageView nav_back;
    EditText dob;
    Button save;
    EditText first_name;
    EditText last_name;
    EditText unit_no;
    EditText code;
    EditText nhs;
    EditText gender;
    Boolean validation;

    FirebaseFirestore db;

    Add_Child_Controller add_child_controller;

    private DrawerLayout mDrawerLayout;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        db = FirebaseFirestore.getInstance();

        //signout = (Button) findViewById(R.id.signout);
        username = (TextView) findViewById(R.id.username);
        menu = (ImageView) findViewById(R.id.menu);
        nav_back = (ImageView) findViewById(R.id.nav_back);
        dob = (EditText) findViewById(R.id.dob);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        unit_no = (EditText) findViewById(R.id.unit_no);
        code = (EditText) findViewById(R.id.code);
        nhs = (EditText) findViewById(R.id.nhs);
        gender = (EditText) findViewById(R.id.gender);
        save = (Button) findViewById(R.id.save);
        // username.setText(firstname + " " + lastname);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        add_child_controller = new Add_Child_Controller(this);

        // setSupportActionBar(toolbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation = add_child_controller.onSubmit(first_name.getText().toString().trim(), last_name.getText().toString().trim(), unit_no.getText().toString().trim(), code.getText().toString().trim(), dob.getText().toString().trim(), nhs.getText().toString().trim(), gender.getText().toString().trim());
                if(validation) {
                    addChild(first_name.getText().toString().trim(), last_name.getText().toString().trim(), unit_no.getText().toString().trim(), code.getText().toString().trim(), dob.getText().toString().trim(), nhs.getText().toString().trim(), gender.getText().toString().trim());
                } else {
                    Toast.makeText(AddChildActivity.this, "Please fill all fields marked asterick", Toast.LENGTH_LONG).show();
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboard = new Intent(AddChildActivity.this, DashboardActivity.class);
                startActivity(dashboard);
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddChildActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setNavigationDrawer(); // call method
    }

    private void addChild(String firstname, String lastname, String unitno, String bcode, String bdob, String bnhs, String bgender) {
        Log.d("Sign Up", "createUserWithEmail:success");
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String id = sh.getString("id", "");

        Map<String, Object> child = new HashMap<>();
        child.put("firstname", firstname);
        child.put("lastname", lastname);
        child.put("unit_no", unitno);
        child.put("code", bcode);
        child.put("dob", bdob);
        child.put("nhs", bnhs);
        child.put("gender", bgender);
        child.put("user_id", id);


        db.collection("childlist")
                .add(child)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        first_name.setText("");
                        last_name.setText("");
                        unit_no.setText("");
                        code.setText("");
                        dob.setText("");
                        nhs.setText("");
                        gender.setText("");
                        Toast.makeText(AddChildActivity.this, "Data saved successfully...", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Data Not Saved", "Error adding document", e);
                    }
                });
    }

    private void updateLabel(){
        String myFormat="MM/dd/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setNavigationDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.left_drawer); // initiate a Navigation View
        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId(); // get selected menu item's id
                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.add_child) {
                    Intent add_child = new Intent(AddChildActivity.this, AddChildActivity.class);
                    startActivity(add_child);
                } else if (itemId == R.id.appointments) {
                    Intent appointments = new Intent(AddChildActivity.this, AppointmentActivity.class);
                    startActivity(appointments);
                } else if (itemId == R.id.child_list) {
                    Intent child_list = new Intent(AddChildActivity.this, ChildListActivity.class);
                    startActivity(child_list);
                } else if (itemId == R.id.signout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent login = new Intent(AddChildActivity.this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else if (itemId == R.id.dashboard) {
                    Intent dashboard = new Intent(AddChildActivity.this, DashboardActivity.class);
                    startActivity(dashboard);
                }
                // display a toast message with menu item's title
                // Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void firstNameError(String message, boolean isFirstNameEmpty) {

    }

    @Override
    public void lastNameError(String message, boolean isLastNameEmpty) {

    }

    @Override
    public void dobError(String message, boolean isDobEmpty) {

    }
}