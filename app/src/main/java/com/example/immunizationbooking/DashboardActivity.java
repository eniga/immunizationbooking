package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immunizationbooking.Adapter.AppointmentAdapter;
import com.example.immunizationbooking.Adapter.ChildAdapter;
import com.example.immunizationbooking.Data.Vaccines;
import com.example.immunizationbooking.Interface.Delete_Interface;
import com.example.immunizationbooking.Model.AppointmentList;
import com.example.immunizationbooking.Model.ChildList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements Delete_Interface {
    FloatingActionButton float_btn;
    TextView username;
    ImageView menu;
    TextView show_all_appointment;
    TextView show_all_child_list;
    TextView total_child;
    TextView total_appointments;
    LinearLayout no_child_added;
    LinearLayout child_added;
    private DrawerLayout mDrawerLayout;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    RecyclerView appointmentRecyclerView;
    ChildAdapter childAdapter;
    List<ChildList> items;
    List<AppointmentList> appointments;
    AppointmentAdapter appointmentAdapter;
    LinearLayout no_appointment;
    LinearLayout appointment_added;


    Delete_Interface delete_interface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        delete_interface = this;

        username = (TextView) findViewById(R.id.username);
        menu = (ImageView) findViewById(R.id.menu);
        float_btn = (FloatingActionButton) findViewById(R.id.float_btn);
        show_all_appointment = (TextView) findViewById(R.id.show_all_appointment);
        show_all_child_list = (TextView) findViewById(R.id.show_all_child_list);
        total_child = (TextView) findViewById(R.id.total_child);
        no_child_added = (LinearLayout) findViewById(R.id.no_child_added);
        child_added = (LinearLayout) findViewById(R.id.child_added);
        total_appointments = (TextView) findViewById(R.id.total_appointments);
        no_appointment = (LinearLayout) findViewById(R.id.no_appointment);
        appointment_added = (LinearLayout) findViewById(R.id.appointment_added);

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String firstname = sh.getString("firstname", "");
        String lastname = sh.getString("lastname", "");
        String id = sh.getString("id", "");


        username.setText(firstname + " " + lastname);

        db.collection("childlist")
                .whereEqualTo("user_id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i("TOTAL", String.valueOf(task.getResult().size()));
                            total_child.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });

        db.collection("appointments")
                .whereEqualTo("user_id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i("TOTAL", String.valueOf(task.getResult().size()));
                            total_appointments.setText(String.valueOf(task.getResult().size()));
                        }
                    }
                });
        db.collection("childlist")
                .whereEqualTo("user_id", id)
                .limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recyclerView = (RecyclerView) findViewById(R.id.child_item);
                            items = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("VID:", document.getId());
                                String id = document.getId();
                                String nfirstname = document.getString("firstname");
                                String nlastname = document.getString("lastname");
                                String dob = document.getString("dob");
                                String gender = document.getString("gender");
                                String code = document.getString("code");
                                String unit_no = document.getString("unit_no");
                                String nhs = document.getString("nhs");
                                Log.i("DOC", nfirstname);
                                items.add(new ChildList(id, nfirstname, nlastname, dob, gender, code, unit_no, nhs));
                            }

                            childAdapter = new ChildAdapter(items, DashboardActivity.this, delete_interface);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, RecyclerView.VERTICAL, false));
                            recyclerView.setAdapter(childAdapter);
                            if(task.getResult().size() > 0) {
                                child_added.setVisibility(View.VISIBLE);
                                no_child_added.setVisibility(View.GONE);
                            } else {
                                child_added.setVisibility(View.GONE);
                                no_child_added.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("appointments")
                .whereEqualTo("user_id", id)
                .limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            appointmentRecyclerView = (RecyclerView) findViewById(R.id.appointment_item);
                            appointments = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("VID:", document.getId());
                                String id = document.getId();
                                String vaccine_date = document.getString("date");
                                String immunizer = document.getString("immunizer");
                                String venue = document.getString("venue");
                                String vaccine = document.getString("vaccine");
                                String child_name = document.getString("child_name");
                                appointments.add(new AppointmentList(immunizer, vaccine_date, vaccine, venue, child_name));
                            }

                            appointmentAdapter = new AppointmentAdapter(appointments, DashboardActivity.this);
                            appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, RecyclerView.VERTICAL, false));
                            appointmentRecyclerView.setAdapter(appointmentAdapter);
                            if(task.getResult().size() > 0) {
                                appointment_added.setVisibility(View.VISIBLE);
                                no_appointment.setVisibility(View.GONE);
                            } else {
                                appointment_added.setVisibility(View.GONE);
                                no_appointment.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // setSupportActionBar(toolbar);

        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_child = new Intent(DashboardActivity.this, AddChildActivity.class);
                startActivity(add_child);
            }
        });

        show_all_child_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent child_list = new Intent(DashboardActivity.this, ChildListActivity.class);
                startActivity(child_list);
            }
        });

        show_all_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appointments = new Intent(DashboardActivity.this, AppointmentActivity.class);
                startActivity(appointments);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        setNavigationDrawer(); // call method
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
                    Intent add_child = new Intent(DashboardActivity.this, AddChildActivity.class);
                    startActivity(add_child);
                } else if (itemId == R.id.appointments) {
                    Intent appointments = new Intent(DashboardActivity.this, AppointmentActivity.class);
                    startActivity(appointments);
                } else if (itemId == R.id.child_list) {
                    Intent child_list = new Intent(DashboardActivity.this, ChildListActivity.class);
                    startActivity(child_list);
                } else if (itemId == R.id.signout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent login = new Intent(DashboardActivity.this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else if (itemId == R.id.dashboard) {
                    Intent dashboard = new Intent(DashboardActivity.this, DashboardActivity.class);
                    startActivity(dashboard);
                }
                // display a toast message with menu item's title
                // Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void deleteChild(String document) {
        Log.i("DOC", document);
        db.collection("childlist").document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETE DONE", "DocumentSnapshot successfully deleted!");
                            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                            String id = sh.getString("id", "");
                            db.collection("childlist")
                                    .whereEqualTo("user_id", id)
                                    .limit(2)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                recyclerView = (RecyclerView) findViewById(R.id.child_item);
                                                items = new ArrayList<>();

                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.i("VID:", document.getId());
                                                    String id = document.getId();
                                                    String nfirstname = document.getString("firstname");
                                                    String nlastname = document.getString("lastname");
                                                    String dob = document.getString("dob");
                                                    String gender = document.getString("gender");
                                                    String code = document.getString("code");
                                                    String unit_no = document.getString("unit_no");
                                                    String nhs = document.getString("nhs");
                                                    Log.i("DOC", nfirstname);
                                                    items.add(new ChildList(id, nfirstname, nlastname, dob, gender, code, unit_no, nhs));
                                                }

                                                childAdapter = new ChildAdapter(items, DashboardActivity.this, delete_interface);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, RecyclerView.VERTICAL, false));
                                                recyclerView.setAdapter(childAdapter);
                                                if(task.getResult().size() > 0) {
                                                    child_added.setVisibility(View.VISIBLE);
                                                    no_child_added.setVisibility(View.GONE);
                                                } else {
                                                    child_added.setVisibility(View.GONE);
                                                    no_child_added.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                Log.d("", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DELETE FAILED", "Error deleting document", e);
                    }
                });
    }
}