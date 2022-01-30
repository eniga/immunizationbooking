package com.example.immunizationbooking.Fragrament;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.immunizationbooking.R;

public class EditFragment extends Fragment {
    ImageView nav_back;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        // title = rootView.findViewById(R.id.bar_title);

        nav_back = rootView.findViewById(R.id.nav_back);
        nav_back();

        return rootView;
    }

    public void nav_back() {
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}