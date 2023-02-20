package com.lixer.womensafetyapp;


import static com.lixer.womensafetyapp.MainActivity.home_nav_btn;
import static com.lixer.womensafetyapp.MainActivity.home_nav_txt;
import static com.lixer.womensafetyapp.MainActivity.location_nav_btn;
import static com.lixer.womensafetyapp.MainActivity.location_nav_txt;
import static com.lixer.womensafetyapp.MainActivity.selectedNavButton;
import static com.lixer.womensafetyapp.MainActivity.setting_nav_btn;
import static com.lixer.womensafetyapp.MainActivity.setting_nav_txt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class home_screen extends Fragment implements View.OnClickListener{


    private ConstraintLayout notification_btn;
    private ConstraintLayout alert_btn_active;
    private ConstraintLayout alert_btn_unactive;
    private ConstraintLayout call_dialog_btn;
    private ConstraintLayout setting_btn;
    private ConstraintLayout about_us_btn;

    private Viberation vibe;

    private CardView create_report_btn;
    private CardView recent_report_btn;

    private Dialog dialog = null;


    private Boolean alert_active= false;

    private int i=0;





    private ImageView notification_img;

    private TextView alert_respense_txt;

    private View rootV;

    private int container;
    private Cursor cursor;

    public home_screen(int containor) {
        this.container= containor;
    }

    public home_screen() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootV= inflater.inflate(R.layout.home_screen, container, false);


        notification_btn    = rootV.findViewById(R.id.notification_btn);
        notification_img    = rootV.findViewById(R.id.notification_icon);
        alert_btn_active    = rootV.findViewById(R.id.alert_button_selected);
        alert_btn_unactive  = rootV.findViewById(R.id.alert_button_unselected);
        alert_respense_txt  = rootV.findViewById(R.id.alert_response_txt);
        create_report_btn   = rootV.findViewById(R.id.card_report_btn);
        recent_report_btn   = rootV.findViewById(R.id.card_recent_report_btn);
        call_dialog_btn     = rootV.findViewById(R.id.vol_btn);
        setting_btn         = rootV.findViewById(R.id.setting_btn);
        about_us_btn        = rootV.findViewById(R.id.about_us_btn);

        vibe = new Viberation(rootV.getContext());

        notification_btn.setOnClickListener(this);
        alert_btn_active.setOnClickListener(this);
        alert_btn_unactive.setOnClickListener(this);
        alert_respense_txt.setOnClickListener(this);
        create_report_btn.setOnClickListener(this);
        recent_report_btn.setOnClickListener(this);
        call_dialog_btn.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
        about_us_btn.setOnClickListener(this);


        LocalBroadcastManager.getInstance(rootV.getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("activity-says-hi"));


        AlertCheck();
        user();




        return rootV;
    }
    private String user_id;
    private void user() {
       cursor = new user_db(rootV.getContext()).viewMethod();
       cursor.moveToNext();
       user_id = String.valueOf(cursor.getInt(1));

    }

    private void AlertCheck() {


        Cursor cursor = new settings_data_base(rootV.getContext()).viewMethod();
        if(cursor.moveToNext()){
            int alert_check = cursor.getInt(4);
            if (alert_check==1){
                onActiveAlert();
//                onAlertDisable();
            }else{
                onDeactiveAlert();
            }
        }


    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            doSomethingCauseVolumeKeyPressed();

        }
    };







    private void doSomethingCauseVolumeKeyPressed() {

        i++;
        Handler handler = new Handler();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                i = 0;
            }
        };

        if (i == 1) {
            //Single click
            handler.postDelayed(r, 250);
        } else if (i == 2) {
            //Double click
            i = 0;
            Boolean alertInserted = new settings_data_base(rootV.getContext())
                    .updateMethod("4","1",1);
            if (alertInserted){
                onActiveAlert();
                vibe.playViberation(1500);
            }
        }


    }

//    public void myOnKeyDown(int key_code){
//
//        Toast.makeText(rootV.getContext(), "KeyP", Toast.LENGTH_SHORT).show();
//        //do whatever you want here
//    }





    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            case R.id.notification_btn:
                vibe.playViberation(50);
                intent = new Intent(rootV.getContext(),notification_activity.class);
                rootV.getContext().startActivity(intent);
                break;
            case R.id.alert_button_selected:
                vibe.playViberation(50);



                if(alert_active){

                dialog = new Dialog(rootV.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.alert_pressed_dialoag_box);
                dialog.getWindow().getAttributes().width= WindowManager.LayoutParams.MATCH_PARENT;

                dialog.show();


                ConstraintLayout yes_btn=dialog.findViewById(R.id.yes_btn);
                TextView cancel_btn=dialog.findViewById(R.id.cancel_btn);
                


                TextView alert_mes=dialog.findViewById(R.id.alert_mes);


                alert_mes.setText("do you want to cancel \nalert");

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Boolean alertInserted = new settings_data_base(rootV.getContext())
                                .updateMethod("4","1",0);
                        if (alertInserted){
                            dialog.dismiss();
                            onDeactiveAlert();
                            vibe.playViberation(50);
                            vibe.stopViberation();
                        }

                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        vibe.playViberation(50);
                    }
                });



                }
                break;
            case R.id.alert_button_unselected:
                vibe.playViberation(50);

//                alert_btn_active.setVisibility(View.VISIBLE);
//                alert_btn_unactive.setVisibility(View.INVISIBLE);

                if(!alert_active){


                dialog = new Dialog(rootV.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.alert_pressed_dialoag_box);
                dialog.getWindow().getAttributes().width= WindowManager.LayoutParams.MATCH_PARENT;

                dialog.show();


                ConstraintLayout yes_btn=dialog.findViewById(R.id.yes_btn);
                TextView cancel_btn=dialog.findViewById(R.id.cancel_btn);



                TextView alert_mes=dialog.findViewById(R.id.alert_mes);


                alert_mes.setText("do you want to press alert\nbutton");

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Boolean alertInserted = new settings_data_base(rootV.getContext())
                                .updateMethod("4","1",1);
                        if (alertInserted){
                            dialog.dismiss();
                            onActiveAlert();
                            vibe.playViberation(1500);
                            alertVibrate();
                        }



                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        vibe.playViberation(50);
                    }
                });






                }



                break;
            case R.id.card_report_btn:
                vibe.playViberation(50);
                 intent = new Intent(rootV.getContext(),create_report_activity.class);
                rootV.getContext().startActivity(intent);
                break;
            case R.id.card_recent_report_btn:
                vibe.playViberation(50);
                 intent = new Intent(rootV.getContext(),recent_report_activity.class);
                rootV.getContext().startActivity(intent);
                break;
            case R.id.vol_btn:

                vibe.playViberation(50);

                final Dialog dialog = new Dialog(rootV.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.call_btn_dialoag_box);
                dialog.getWindow().getAttributes().width= WindowManager.LayoutParams.MATCH_PARENT;

                dialog.show();
                ConstraintLayout phone1=dialog.findViewById(R.id.phone_1);
                ConstraintLayout phone2=dialog.findViewById(R.id.phone_2);

                ConstraintLayout phone_call_btn_1=dialog.findViewById(R.id.yes_btn);
                ConstraintLayout phone_call_btn_2=dialog.findViewById(R.id.phone_btn_2);

                TextView phone_txt_1=dialog.findViewById(R.id.cancel_btn);
                TextView phone_txt_2=dialog.findViewById(R.id.phone_num_txt_2);

                phone_call_btn_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callAction = new Intent(Intent.ACTION_DIAL);
                        callAction.setData(Uri.parse("tel:"+phone_txt_1.getText().toString()));
                        startActivity(callAction);

                    }
                });

                phone_call_btn_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callAction = new Intent(Intent.ACTION_DIAL);
                        callAction.setData(Uri.parse("tel:"+phone_txt_2.getText().toString()));
                        startActivity(callAction);

                    }
                });






                break;
            case R.id.setting_btn:
                vibe.playViberation(50);
                selectedNavButton = R.id.nav_setting_button;
                getActivity().getSupportFragmentManager().beginTransaction().replace(container, new setting_screen()).commit();
                home_nav_btn.setImageDrawable(getActivity().getDrawable(R.drawable.home_unselected));
                location_nav_btn.setImageDrawable(getActivity().getDrawable(R.drawable.location_unselected));
                setting_nav_btn.setImageDrawable(getActivity().getDrawable(R.drawable.setting_selected));
//        profile_nav_btn.setImageDrawable(getDrawable(R.drawable.pro_file_unselected));

                home_nav_txt.setTextColor(getActivity().getResources().getColor(R.color.lighter_text_color));
                location_nav_txt.setTextColor(getActivity().getResources().getColor(R.color.lighter_text_color));
                setting_nav_txt.setTextColor(getActivity().getResources().getColor(R.color.primary_color));
//        pf_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
                break;
            case R.id.about_us_btn:



                break;




    }}
     private int itimer= 0;
    private void alertVibrate() {

        Runnable r = new Runnable() {
            @Override
            public void run() {

                vibe.playViberation(1500);
                itimer++;
                alertVibrate();
            }
        };
        if(itimer>=5){
            itimer = 0;
        }else {
            new Handler().postDelayed(r,2500);

        }
    }

    private  void onActiveAlert(){


        alert_btn_unactive.setClickable(false);
        alert_btn_unactive.animate().alpha(0).setDuration(300).start();
        alert_btn_active.animate().alpha(1).setDuration(300).start();
        alert_btn_active.setVisibility(View.VISIBLE);
        alert_btn_active.setClickable(true);
        alert_active = true;
        httpBackgroundAsync h2= new httpBackgroundAsync("https://fyp.parkh.net/api/alert_post/"+user_id);
        h2.execute();



    }
    private  void onDeactiveAlert(){


        alert_btn_active.setClickable(false);
        alert_btn_active.animate().alpha(0).setDuration(300).start();
        alert_btn_unactive.animate().alpha(1).setDuration(300).start();
        alert_btn_unactive.setAlpha(1);
        alert_btn_unactive.setVisibility(View.VISIBLE);
        alert_btn_unactive.setClickable(true);
        alert_active = false;

        httpBackgroundAsync h2= new httpBackgroundAsync("https://fyp.parkh.net/api/disable_alert_post/"+user_id);
        h2.execute();

    }
//    private void onAlertDisable() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Boolean alertInserted = new settings_data_base(rootV.getContext())
//                        .updateMethod("4","1",0);
//                if (alertInserted){
//                    onDeactiveAlert();
//                }
//            }
//        },10000);
//
//    }


    public class httpBackgroundAsync extends AsyncTask<Void,Void,String> {

        String url;
        String json_string;



        public httpBackgroundAsync(String url) {
            this.url = url;

        }

        public httpBackgroundAsync() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(Void... voids) {

            URL url2 = null;

            try{
                url2 = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = bufferedReader.readLine()) != null){
                    stringBuilder.append(json_string+"/n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        if (s  != null ){



            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){



                }else{

                    Toast.makeText(rootV.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }else{
            Toast.makeText(rootV.getContext(), "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
            httpBackgroundAsync h2= new httpBackgroundAsync("https://fyp.parkh.net/api/disable_alert_post/"+user_id);
            h2.execute();
        }

        }



    }


}