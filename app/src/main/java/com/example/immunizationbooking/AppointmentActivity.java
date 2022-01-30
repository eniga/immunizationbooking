package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immunizationbooking.Adapter.AppointmentAdapter;
import com.example.immunizationbooking.Adapter.ChildAdapter;
import com.example.immunizationbooking.Model.AppointmentList;
import com.example.immunizationbooking.Model.ChildList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {
    TextView username;
    ImageView menu;
    LinearLayout no_appointment;
    ScrollView appointment_items;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    RecyclerView appointmentRecyclerView;
    List<AppointmentList> appointments;
    AppointmentAdapter appointmentAdapter;

    private DrawerLayout mDrawerLayout;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        db = FirebaseFirestore.getInstance();

        username = (TextView) findViewById(R.id.username);
        menu = (ImageView) findViewById(R.id.menu);
        no_appointment = (LinearLayout) findViewById(R.id.no_appointment);
        appointment_items = (ScrollView) findViewById(R.id.appointments);

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String firstname = sh.getString("firstname", "");
        String lastname = sh.getString("lastname", "");
        String id = sh.getString("id", "");


        username.setText(firstname + " " + lastname);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        db.collection("appointments")
                .whereEqualTo("user_id", id)
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

                            appointmentAdapter = new AppointmentAdapter(appointments, AppointmentActivity.this);
                            appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(AppointmentActivity.this, RecyclerView.VERTICAL, false));
                            appointmentRecyclerView.setAdapter(appointmentAdapter);
                            if(task.getResult().size() > 0) {
                                appointment_items.setVisibility(View.VISIBLE);
                                no_appointment.setVisibility(View.GONE);
                            } else {
                                appointment_items.setVisibility(View.GONE);
                                no_appointment.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
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
                    Intent add_child = new Intent(AppointmentActivity.this, AddChildActivity.class);
                    startActivity(add_child);
                } else if (itemId == R.id.appointments) {
                    Intent appointments = new Intent(AppointmentActivity.this, AppointmentActivity.class);
                    startActivity(appointments);
                } else if (itemId == R.id.child_list) {
                    Intent child_list = new Intent(AppointmentActivity.this, ChildListActivity.class);
                    startActivity(child_list);
                } else if (itemId == R.id.signout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent login = new Intent(AppointmentActivity.this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else if (itemId == R.id.dashboard) {
                    Intent dashboard = new Intent(AppointmentActivity.this, DashboardActivity.class);
                    startActivity(dashboard);
                }
                // display a toast message with menu item's title
                // Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}