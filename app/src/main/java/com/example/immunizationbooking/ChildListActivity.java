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

import com.example.immunizationbooking.Adapter.ChildAdapter;
import com.example.immunizationbooking.Interface.Delete_Interface;
import com.example.immunizationbooking.Model.ChildList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChildListActivity extends AppCompatActivity implements Delete_Interface {
    TextView username;
    ImageView menu;
    ScrollView childitems;
    LinearLayout no_child_added;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ChildAdapter childAdapter;
    List<ChildList> items;
    Delete_Interface delete_interface;

    private DrawerLayout mDrawerLayout;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);

        db = FirebaseFirestore.getInstance();
        delete_interface = this;

        //signout = (Button) findViewById(R.id.signout);
        username = (TextView) findViewById(R.id.username);
        menu = (ImageView) findViewById(R.id.menu);
        childitems = (ScrollView) findViewById(R.id.childitems);
        no_child_added = (LinearLayout) findViewById(R.id.no_child_added);

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
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recyclerView = (RecyclerView) findViewById(R.id.child_item);
                            items = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("ID", document.getId());
                                String id = document.getId();
                                String nfirstname = document.getString("firstname");
                                String nlastname = document.getString("lastname");
                                String dob = document.getString("dob");
                                String gender = document.getString("gender");
                                String code = document.getString("code");
                                String unit_no = document.getString("unit_no");
                                String nhs = document.getString("nhs");
                                items.add(new ChildList(id, nfirstname, nlastname, dob, gender, code, unit_no, nhs));
                            }

                            childAdapter = new ChildAdapter(items, ChildListActivity.this, delete_interface);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChildListActivity.this, RecyclerView.VERTICAL, false));
                            recyclerView.setAdapter(childAdapter);
                            if(task.getResult().size() > 0) {
                                childitems.setVisibility(View.VISIBLE);
                                no_child_added.setVisibility(View.GONE);
                            } else {
                                childitems.setVisibility(View.GONE);
                                no_child_added.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // setSupportActionBar(toolbar);

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
                    Intent add_child = new Intent(ChildListActivity.this, AddChildActivity.class);
                    startActivity(add_child);
                } else if (itemId == R.id.appointments) {
                    Intent appointments = new Intent(ChildListActivity.this, AppointmentActivity.class);
                    startActivity(appointments);
                } else if (itemId == R.id.child_list) {
                    Intent child_list = new Intent(ChildListActivity.this, ChildListActivity.class);
                    startActivity(child_list);
                } else if (itemId == R.id.signout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent login = new Intent(ChildListActivity.this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else if (itemId == R.id.dashboard) {
                    Intent dashboard = new Intent(ChildListActivity.this, DashboardActivity.class);
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

                                                childAdapter = new ChildAdapter(items, ChildListActivity.this, delete_interface);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(ChildListActivity.this, RecyclerView.VERTICAL, false));
                                                recyclerView.setAdapter(childAdapter);
                                                if(task.getResult().size() > 0) {
                                                    childitems.setVisibility(View.VISIBLE);
                                                    no_child_added.setVisibility(View.GONE);
                                                } else {
                                                    childitems.setVisibility(View.GONE);
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