package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immunizationbooking.Controller.Add_Child_Controller;
import com.example.immunizationbooking.View.Add_Child_View_Interface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditChildActivity extends AppCompatActivity implements Add_Child_View_Interface {
    ImageView nav_back;
    TextView firstname;
    TextView lastname;
    TextView unit_no;
    TextView code;
    TextView dob;
    TextView nhs;
    TextView gender;
    Button save;
    Boolean validation;
    String id;

    Add_Child_Controller add_child_controller;

    FirebaseFirestore db;
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        db = FirebaseFirestore.getInstance();

        nav_back = (ImageView) findViewById(R.id.nav_back);
        firstname = (TextView) findViewById(R.id.first_name);
        lastname = (TextView) findViewById(R.id.last_name);
        unit_no = (TextView) findViewById(R.id.unit_no);
        code = (TextView) findViewById(R.id.code);
        dob = (TextView) findViewById(R.id.dob);
        nhs = (TextView) findViewById(R.id.nhs);
        gender = (TextView) findViewById(R.id.gender);
        save = (Button) findViewById(R.id.save);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            id = extra.getString("id");
            firstname.setText(extra.getString("firstname"));
            lastname.setText(extra.getString("lastname"));
            unit_no.setText(extra.getString("unit_no"));
            code.setText(extra.getString("code"));
            dob.setText(extra.getString("dob"));
            nhs.setText(extra.getString("nhs"));
            gender.setText(extra.getString("gender"));
        }

        add_child_controller = new Add_Child_Controller(this);

        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboard = new Intent(EditChildActivity.this, DashboardActivity.class);
                startActivity(dashboard);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation = add_child_controller.onSubmit(firstname.getText().toString().trim(), lastname.getText().toString().trim(), unit_no.getText().toString().trim(), code.getText().toString().trim(), dob.getText().toString().trim(), nhs.getText().toString().trim(), gender.getText().toString().trim());
                if(validation) {
                    Log.i("BILL", "Testing");
                    editChild(firstname.getText().toString().trim(), lastname.getText().toString().trim(), unit_no.getText().toString().trim(), code.getText().toString().trim(), dob.getText().toString().trim(), nhs.getText().toString().trim(), gender.getText().toString().trim());
                } else {
                    Toast.makeText(EditChildActivity.this, "Please fill all fields marked asterick", Toast.LENGTH_LONG).show();
                }
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
                new DatePickerDialog(EditChildActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void editChild(String firstname, String lastname, String unitno, String bcode, String bdob, String bnhs, String bgender) {
        Log.d("Sign Up", "createUserWithEmail:success");
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data

        Map<String, Object> child = new HashMap<>();
        child.put("firstname", firstname);
        child.put("lastname", lastname);
        child.put("unit_no", unitno);
        child.put("code", bcode);
        child.put("dob", bdob);
        child.put("nhs", bnhs);
        child.put("gender", bgender);

        DocumentReference childListRef = db.collection("childlist").document(id);
            childListRef.update(child)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditChildActivity.this, "Data saved successfully...", Toast.LENGTH_LONG).show();
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