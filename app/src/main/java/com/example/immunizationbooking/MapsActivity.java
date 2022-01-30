package com.example.immunizationbooking;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Button book;
    private GoogleMap mMap;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        db = FirebaseFirestore.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        book = (Button) findViewById(R.id.book);
        Bundle extra = getIntent().getExtras();
        String id = extra.getString("id");
        String firstname = extra.getString("firstname");
        String lastname = extra.getString("lastname");
        String dob = extra.getString("dob");
        String gender = extra.getString("gender");
        String vaccine = extra.getString("name");
        String user_id = sh.getString("id", "");
        Log.i("VAC", vaccine);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date curr_date = new Date();
                Map<String, Object> appointment = new HashMap<>();
                appointment.put("vaccine", vaccine);
                appointment.put("date", dateFormat.format(curr_date));
                appointment.put("immunizer", "Carl Kim");
                appointment.put("venue", "12 Crafford Lane, Bradford");
                appointment.put("user_id", user_id);
                appointment.put("child", id);
                appointment.put("child_name", firstname + " " + lastname);
                appointment.put("dob", dob);
                appointment.put("gender", gender);


                db.collection("appointments")
                        .add(appointment)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MapsActivity.this, "Data saved successfully...", Toast.LENGTH_LONG).show();

                                Intent immunization = new Intent(MapsActivity.this, ImmunizationActivity.class);
                                immunization.putExtra("id", id);
                                immunization.putExtra("firstname", firstname);
                                immunization.putExtra("lastname", lastname);
                                immunization.putExtra("dob", dob);
                                immunization.putExtra("gender", gender);
                                startActivity(immunization);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Data Not Saved", "Error adding document", e);
                            }
                        });
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(53.7, -1.8);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Bradford"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}