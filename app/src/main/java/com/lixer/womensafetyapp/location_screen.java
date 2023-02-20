package com.lixer.womensafetyapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class location_screen extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment



        View v= inflater.inflate(R.layout.location_screen, container, false);

//        backFromProfile();

        return v;
    }


    // back press method

//    private void backFromProfile() {
//
//        profile_back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
//
//    }





}