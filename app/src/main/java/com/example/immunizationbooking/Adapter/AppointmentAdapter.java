package com.example.immunizationbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.immunizationbooking.ImmunizationActivity;
import com.example.immunizationbooking.Interface.Delete_Interface;
import com.example.immunizationbooking.MapsActivity;
import com.example.immunizationbooking.Model.AppointmentList;
import com.example.immunizationbooking.Model.VaccineList;
import com.example.immunizationbooking.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<AppointmentList> list;
    private Context context;
    private Context parentContext;

    public AppointmentAdapter(List<AppointmentList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_list, parent, false);
        this.parentContext = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentList mylist = this.list.get(position);
        holder.vaccine.setText(mylist.getVaccine());
        holder.immunizer.setText("Immunizer: " + mylist.getImmunizer());
        holder.vaccine_date.setText("Date: " + mylist.getVaccine_date());
        holder.venue.setText("Venue: " + mylist.getVenue());
        holder.venue.setText("Venue: " + mylist.getVenue());
        holder.child_name.setText("Child Name: " + mylist.getChild_name());
        Context context = this.context;

        holder.status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Base", "KIT");
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vaccine;
        public TextView immunizer;
        public Button status_btn;
        public TextView venue;
        public TextView vaccine_date;
        public TextView child_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.vaccine = (TextView) itemView.findViewById(R.id.vaccine);
            this.status_btn = (Button) itemView.findViewById(R.id.status);
            this.immunizer = (TextView) itemView.findViewById(R.id.immunizer);
            this.vaccine_date = (TextView) itemView.findViewById(R.id.date);
            this.venue = (TextView) itemView.findViewById(R.id.venue);
            this.child_name = (TextView) itemView.findViewById(R.id.childname);
        }
    }
}



