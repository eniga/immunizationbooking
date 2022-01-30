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
import com.example.immunizationbooking.Model.VaccineList;
import com.example.immunizationbooking.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.ViewHolder> {
    private List<VaccineList> list;
    private Context context;
    private Context parentContext;

    public VaccineAdapter(List<VaccineList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vaccines_list, parent, false);
        this.parentContext = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VaccineList mylist = this.list.get(position);
        holder.name.setText(mylist.getVaccine_name());
        Context context = this.context;

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(context, MapsActivity.class);
                Log.i("NMAME", mylist.getVaccine_name());
                map.putExtra("name", mylist.getVaccine_name());
                map.putExtra("id", mylist.getChildId());
                map.putExtra("firstname", mylist.getFirstname());
                map.putExtra("lastname", mylist.getLastname());
                map.putExtra("dob", mylist.getDob());
                map.putExtra("gender", mylist.getGender());

                context.startActivity(map);
                Log.i("Base", "KIT");
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public Button book;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.vaccine);
            this.book = (Button) itemView.findViewById(R.id.book);
        }
    }
}


