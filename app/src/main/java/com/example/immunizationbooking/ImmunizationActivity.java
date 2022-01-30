package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.immunizationbooking.Adapter.ChildAdapter;
import com.example.immunizationbooking.Adapter.VaccineAdapter;
import com.example.immunizationbooking.Data.Vaccines;
import com.example.immunizationbooking.Model.ChildList;
import com.example.immunizationbooking.Model.VaccineList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImmunizationActivity extends AppCompatActivity {
    TextView tdob;
    TextView tname;
    TextView tgender;
    ImageView menu;
    private DrawerLayout mDrawerLayout;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;
    Vaccines vaccines;
    RecyclerView recyclerView;
    VaccineAdapter vaccineAdapter;
    List<VaccineList> items;
    LinearLayout no_immunization;
    LinearLayout child_added;
    TextView appointments;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunization);
        db = FirebaseFirestore.getInstance();

        vaccines = new Vaccines();

        Bundle extra = getIntent().getExtras();
        String dob = extra.getString("dob");
        String name = extra.getString("firstname") + " " + extra.getString("lastname");
        String firstname = extra.getString("firstname");
        String lastname = extra.getString("lastname");
        String gender = extra.getString("gender");
        String id = extra.getString("id");
        String replaceString = dob.replace('/',' ');//replaces all occurrences of 'a' to 'e'
        tdob = (TextView) findViewById(R.id.dob);
        tname = (TextView) findViewById(R.id.name);
        tgender = (TextView) findViewById(R.id.gender);
        menu = (ImageView) findViewById(R.id.menu);
        no_immunization = (LinearLayout) findViewById(R.id.no_immunization);
        child_added = (LinearLayout) findViewById(R.id.child_added);
        appointments = (TextView) findViewById(R.id.appointments);

        tdob.setText(dob);
        tname.setText(name);
        tgender.setText(gender);

        try {
            Log.i("DOB", String.valueOf(getDays(replaceString)));
            Log.i("Vaccines: ", Arrays.toString(vaccines(getDays(replaceString))));

            String[] vaccines = vaccines(getDays(replaceString));

            db.collection("appointments")
                    .whereEqualTo("child", id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                appointments.setText("Appointments: " + String.valueOf(task.getResult().size()));
                                List<String> vaccines_items = new ArrayList<>();
                                int i = 0;

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.i("DOC", document.getString("vaccine"));
                                    vaccines_items.add(document.getString("vaccine"));
                                }

                                String[] myArray = new String[vaccines_items.size()];
                                vaccines_items.toArray(myArray);

                                List<String> new_vaccines_items = new ArrayList<>();
                                for(i = 0; i < vaccines.length; i++) {
                                    if(!check(myArray, vaccines[i])) {
                                        new_vaccines_items.add(vaccines[i]);
                                    }
                                }

                                String[] newMyArray = new String[new_vaccines_items.size()];
                                new_vaccines_items.toArray(newMyArray);

                                Log.i("NEW", Arrays.toString(newMyArray));

                                recyclerView = (RecyclerView) findViewById(R.id.child_item);
                                items = new ArrayList<>();
                                for(String vaccine : newMyArray) {
                                    items.add(new VaccineList(vaccine, id, firstname, lastname, gender, dob));
                                }

                                vaccineAdapter = new VaccineAdapter(items, ImmunizationActivity.this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ImmunizationActivity.this, RecyclerView.VERTICAL, false));
                                recyclerView.setAdapter(vaccineAdapter);
                                if(newMyArray.length > 0) {
                                    child_added.setVisibility(View.VISIBLE);
                                    no_immunization.setVisibility(View.GONE);
                                } else {
                                    child_added.setVisibility(View.GONE);
                                    no_immunization.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        setNavigationDrawer(); // call method
    }

    private static String[] combineString(String[] first, String[] second){
        int length = first.length + second.length;
        String[] result = new String[length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
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
                    Intent add_child = new Intent(ImmunizationActivity.this, AddChildActivity.class);
                    startActivity(add_child);
                } else if (itemId == R.id.appointments) {
                    Intent appointments = new Intent(ImmunizationActivity.this, AppointmentActivity.class);
                    startActivity(appointments);
                } else if (itemId == R.id.child_list) {
                    Intent child_list = new Intent(ImmunizationActivity.this, ChildListActivity.class);
                    startActivity(child_list);
                } else if (itemId == R.id.signout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent login = new Intent(ImmunizationActivity.this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else if (itemId == R.id.dashboard) {
                    Intent dashboard = new Intent(ImmunizationActivity.this, DashboardActivity.class);
                    startActivity(dashboard);
                }
                // display a toast message with menu item's title
                // Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private int getDays(String date) throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        Date curr_date = new Date();
        Log.i("MOD", dateFormat.format(curr_date));
        String inputString1 = dateFormat.format(curr_date);
        String inputString2 = date;

        Date date1 = myFormat.parse(inputString1);
        Date date2 = myFormat.parse(inputString2);
        long diff = date2.getTime() > date1.getTime()? date2.getTime() - date1.getTime() : date1.getTime() - date2.getTime();
        return Integer.parseInt(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
    }

    private boolean check(String[] arr, String toCheckValue)
    {
        return Arrays.asList(arr).contains(toCheckValue);
    }

    private String[] vaccines(int days) {
        String[] child_vaccines = new String[0];
        if(days == 56) {
            child_vaccines = vaccines.eight;
        } else if(days >= 84 && days > 56) {
            // child_vaccines = combineString(vaccines.eight, vaccines.twelve);
            child_vaccines = vaccines.twelve;
        } else if(days >= 112) {
            // String[] arr = combineString(vaccines.eight, vaccines.twelve);
            // child_vaccines = combineString(arr, vaccines.sixteen);

            child_vaccines = vaccines.sixteen;
        }

        return child_vaccines;
    }
}