package com.lixer.womensafetyapp;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class setting_screen extends Fragment implements View.OnClickListener{



    private ImageView back_btn;
    private ImageView push_notify;
    private ImageView vol_btn_img;
    private ImageView power_btn_img;
    private ConstraintLayout vol_btn;
    private ConstraintLayout power_btn;
    private ConstraintLayout policy_btn;

    private Viberation vibe;

    private View rootV;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment



        rootV= inflater.inflate(R.layout.setting_screen, container, false);

        vibe  = new Viberation(rootV.getContext());



        back_btn = rootV.findViewById(R.id.recent_report_back_btn);
        power_btn = rootV.findViewById(R.id.power_btn);
        vol_btn =rootV.findViewById(R.id.vol_btn);
        push_notify = rootV.findViewById(R.id.push_notify_btn);
        policy_btn = rootV.findViewById(R.id.policy_btn);

        vol_btn_img = rootV.findViewById(R.id.vol_btn_img);
        power_btn_img = rootV.findViewById(R.id.power_btn_img);


        back_btn.setOnClickListener(this);
        policy_btn.setOnClickListener(this);
        power_btn.setOnClickListener(this);
        vol_btn.setOnClickListener(this);
        push_notify.setOnClickListener(this);






     settings_btn_setting();








        return rootV;
    }

    private void settings_btn_setting() {
        Cursor cursor = new settings_data_base(rootV.getContext()).viewMethod();
        cursor.moveToNext();
        if(cursor.getInt(1)==1){
            powerBtnON();
        }else{
            powerBtnOFF();
        }
        if(cursor.getInt(0)==1){
            volBtnON();
        }else{
            volBtnOFF();
        }
        if(cursor.getInt(2)==1){
            nofityBtnON();
        }else{
            notifyBtnOFF();
        }

    }


    @Override
    public void onClick(View v) {
        Cursor cursor = new settings_data_base(rootV.getContext()).viewMethod();
        cursor.moveToNext();

        switch (v.getId()){

            case R.id.power_btn:
                vibe.playViberation(50);
                if(cursor.getInt(1)==0){
                    powerBtnON();
                }else{
                    powerBtnOFF();
                }
                break;
            case R.id.vol_btn:
                vibe.playViberation(50);
                if(cursor.getInt(0)==0){
                    volBtnON();
                }else{
                    volBtnOFF();
                }
                break;
            case R.id.push_notify_btn:
                vibe.playViberation(50);
                if(cursor.getInt(2)==0){
                    nofityBtnON();
                }else{
                    notifyBtnOFF();
                }
                break;
            case R.id.policy_btn:
                vibe.playViberation(50);
                break;
            case R.id.recent_report_back_btn:
                vibe.playViberation(50);
                getActivity().onBackPressed();
                break;

        }
    }

    private void powerBtnON() {

        new settings_data_base(rootV.getContext()).updateMethod("2","1",1);

        power_btn_img.setImageDrawable(rootV.getContext().getResources().getDrawable(R.drawable.ic_vol_switch_button_selected));





    }

    private void powerBtnOFF() {
        new settings_data_base(rootV.getContext()).updateMethod("2","1",0);
        power_btn_img.setImageDrawable(rootV.getContext().getResources().getDrawable(R.drawable.ic_vol_switch_button_unselected));

    }

    private void volBtnON() {

        new settings_data_base(rootV.getContext()).updateMethod("1","1",1);

        vol_btn_img.setImageDrawable(rootV.getContext().getResources().getDrawable(R.drawable.ic_vol_switch_button_selected));


    }

    private void volBtnOFF() {
        new settings_data_base(rootV.getContext()).updateMethod("1","1",0);
        vol_btn_img.setImageDrawable(rootV.getContext().getResources().getDrawable(R.drawable.ic_vol_switch_button_unselected));

    }

    private void nofityBtnON() {
        new settings_data_base(rootV.getContext()).updateMethod("3","1",1);
        push_notify.setImageDrawable(rootV.getContext().getResources().getDrawable(R.drawable.ic_notification_switch_button_selected));

    }

    private void notifyBtnOFF() {
        new settings_data_base(rootV.getContext()).updateMethod("3","1",0);
        push_notify.setImageDrawable(rootV.getContext().getResources().getDrawable(R.drawable.ic_vol_switch_button_unselected));

    }


}