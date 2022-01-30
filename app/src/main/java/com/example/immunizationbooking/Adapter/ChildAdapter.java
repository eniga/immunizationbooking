package com.example.immunizationbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.immunizationbooking.EditChildActivity;
import com.example.immunizationbooking.Fragrament.EditFragment;
import com.example.immunizationbooking.ImmunizationActivity;
import com.example.immunizationbooking.Interface.Delete_Interface;
import com.example.immunizationbooking.Model.ChildList;
import com.example.immunizationbooking.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import static androidx.core.content.ContextCompat.startActivity;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    private List<ChildList> list;
    private Context context;
    private Context parentContext;
    private Delete_Interface listener;

    public ChildAdapter(List<ChildList> list, Context context, Delete_Interface listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_list, parent, false);
        this.parentContext = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChildList mylist = this.list.get(position);
        holder.name.setText(mylist.getFirstName() + " " + mylist.getLastName());
        holder.dob.setText(mylist.getDob());
        holder.gender.setText(mylist.getGender());
        Context context = this.context;

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.item_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Fragment frag;
                        switch (item.getItemId()) {
                            case R.id.immunization:
                                //handle menu1 click
                                Log.i("Menu", "Immunization");
                                Intent immunization = new Intent(context, ImmunizationActivity.class);
                                Log.i("UWU", mylist.getId());
                                immunization.putExtra("id", mylist.getId());
                                immunization.putExtra("firstname", mylist.getFirstName());
                                immunization.putExtra("lastname", mylist.getLastName());
                                immunization.putExtra("unit_no", mylist.getUnit_no());
                                immunization.putExtra("code", mylist.getCode());
                                immunization.putExtra("dob", mylist.getDob());
                                immunization.putExtra("nhs", mylist.getNhs());
                                immunization.putExtra("gender", mylist.getGender());
                                context.startActivity(immunization);
                                return true;
                            case R.id.edit:
                                //handle menu2 click
                                Log.i("Menu", "Edit");
                                Intent edit_child = new Intent(context, EditChildActivity.class);
                                Log.i("UWU", mylist.getId());
                                edit_child.putExtra("id", mylist.getId());
                                edit_child.putExtra("firstname", mylist.getFirstName());
                                edit_child.putExtra("lastname", mylist.getLastName());
                                edit_child.putExtra("unit_no", mylist.getUnit_no());
                                edit_child.putExtra("code", mylist.getCode());
                                edit_child.putExtra("dob", mylist.getDob());
                                edit_child.putExtra("nhs", mylist.getNhs());
                                edit_child.putExtra("gender", mylist.getGender());
                                context.startActivity(edit_child);
                                return true;
                            case R.id.delete:
                                //handle menu3 click
                                Log.i("Menu", "Delete");
                                View popupView = LayoutInflater.from(context).inflate(R.layout.delete_prompt, null);
                                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                Button btnDismiss = (Button) popupView.findViewById(R.id.cancel);
                                Button btnDelete = (Button) popupView.findViewById(R.id.delete);
                                TextView user_text = (TextView) popupView.findViewById(R.id.user_text);
                                user_text.setText("Do you want to delete: " + mylist.getFirstName() + " " + mylist.getLastName());
                                btnDismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindow.dismiss();
                                    }
                                });

                                btnDelete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        listener.deleteChild(mylist.getId());
                                        popupWindow.dismiss();
                                    }
                                });

                                popupWindow.showAsDropDown(popupView, 0, 0);
                                return true;
                            case R.id.view:
                                //handle menu3 click
                                Log.i("Menu", "Delete");
                                View popupViewData = LayoutInflater.from(context).inflate(R.layout.view_prompt, null);
                                final PopupWindow popupWindowData = new PopupWindow(popupViewData, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                Button btnClose = (Button) popupViewData.findViewById(R.id.cancel);

                                TextView usertext = (TextView) popupViewData.findViewById(R.id.user_text);
                                TextView firstname = (TextView) popupViewData.findViewById(R.id.firstname);
                                TextView lastname = (TextView) popupViewData.findViewById(R.id.lastname);
                                TextView code = (TextView) popupViewData.findViewById(R.id.code);
                                TextView unit_no = (TextView) popupViewData.findViewById(R.id.unit_no);
                                TextView dob = (TextView) popupViewData.findViewById(R.id.dob);
                                TextView nhs = (TextView) popupViewData.findViewById(R.id.nhs);
                                TextView gender = (TextView) popupViewData.findViewById(R.id.gender);
                                usertext.setText(mylist.getFirstName() + " " + mylist.getLastName() + " Details");

                                firstname.setText(mylist.getFirstName());
                                lastname.setText(mylist.getLastName());
                                code.setText(mylist.getCode());
                                unit_no.setText(mylist.getUnit_no());
                                dob.setText(mylist.getDob());
                                nhs.setText(mylist.getNhs());
                                gender.setText(mylist.getGender());

                                btnClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindowData.dismiss();
                                    }
                                });

                                popupWindowData.showAsDropDown(popupViewData, 0, 0);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView dob;
        public TextView gender;
        public ImageView menu;

        public ViewHolder(View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.childname);
            this.dob = (TextView) itemView.findViewById(R.id.dob);
            this.gender = (TextView) itemView.findViewById(R.id.gender);
            this.menu = (ImageView) itemView.findViewById(R.id.menu);
        }
    }
}

