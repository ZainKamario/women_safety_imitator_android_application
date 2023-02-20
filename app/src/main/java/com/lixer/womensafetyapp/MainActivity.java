package com.lixer.womensafetyapp;




import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    public static int PROFILE_MUSIC_ON_OFF = 1;
    public static int PROFILE_SOUND_ON_OFF = 1;
    public static int PROFILE_NOTIFICATION_ON_OFF = 1;

    public Uri ProfileImage =null;
    private BottomNavigationView bottom_nav_bar;
    private View view;
    private int HieghtBottomBar;
    public static boolean OnPause = false;
    public static boolean OnResume = false;
    private Fragment selectedView ;
    public static int selectedNavButton;
    private Guideline guideline;

    private Viberation vibe;


    private String jsonString;




    public static ImageView home_nav_btn, location_nav_btn, setting_nav_btn, profile_nav_btn;
    public static TextView home_nav_txt, location_nav_txt, setting_nav_txt, pf_nav_txt;



    Intent mServiceIntent;
    private SensorServiceForeground mSensorService;

    private ProgressDialog progressDialog;

    Context ctx;

    public Context getCtx() {
        return ctx;
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        //  getWindow().setStatusBarColor(getResources().getColor(R.color.for_status_bar));

//        bottom_nav_bar.setItemRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
//        selectedNavButton = R.id.nav_home_button;
        getSupportFragmentManager().beginTransaction().replace(R.id.containor, new home_screen(R.id.containor)).commit();
        setContentView(R.layout.activity_main);



//        HieghtBottomBar=bottom_nav_bar.getHeight();
//        NetworkCheck networkCheck = new NetworkCheck(MainActivity.this);

//        if (networkCheck.isConnected()) {
//
//            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
//
//        } else {
//            Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
//        }




        ConstraintLayout c1 = findViewById(R.id.nav_home_button);
        ConstraintLayout c2 = findViewById(R.id.nav_location_button);
        ConstraintLayout c3 = findViewById(R.id.nav_setting_button);
//        ConstraintLayout c4 = findViewById(R.id.nav_profile_button);

        vibe = new Viberation(this);


        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
//        c4.setOnClickListener(this);


        home_nav_btn = findViewById(R.id.home_nav_btn);
        location_nav_btn = findViewById(R.id.category_nav_btn);
        setting_nav_btn = findViewById(R.id.activity_nav_btn);
//        profile_nav_btn = findViewById(R.id.profile_nav_btn);


        home_nav_txt = findViewById(R.id.home_nav_txt);
        location_nav_txt = findViewById(R.id.location_nav_txt);
        setting_nav_txt = findViewById(R.id.setting_nav_txt);
//        pf_nav_txt = findViewById(R.id.pf_nav_txt);




//        startService(new Intent(this, Background_services.class));

        mSensorService = new SensorServiceForeground();
        mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                this.startForegroundService(mServiceIntent);
//                startForegroundService(mServiceIntent);
//            } else {
//                startService(mServiceIntent);
//            }

        }


        // LOCATION PERMISSION

        locatiomPermissionCheck();
        setting_data_load();
//         progressDialog= ProgressDialog.show(this, "",
//                "Loading Please Wait....", true);


//        httpBackgroundAsync h=new httpBackgroundAsync("https://fyp.parkh.net/api/user_login/z@g.com/12345");
//        h.execute();





    }


//
//public class httpBackgroundAsync extends AsyncTask<Void,Void,String> {
//
//    String url;
//    String json_string;
//
//
//
//    public httpBackgroundAsync(String url) {
//        this.url = url;
//
//    }
//
//    public httpBackgroundAsync() {
//
//    }
//
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//
//
//    @Override
//    protected String doInBackground(Void... voids) {
//
//        URL url2 = null;
//
//        try{
//            url2 = new URL(url);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
//            InputStream inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((json_string = bufferedReader.readLine()) != null){
//                stringBuilder.append(json_string+"/n");
//            }
//            bufferedReader.close();
//            inputStream.close();
//            httpURLConnection.disconnect();
//            return stringBuilder.toString().trim();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        if ( s != null) {
//            jsonString = s;
//            Log.i("jsonString", jsonString);
//            progressDialog.dismiss();
//        }    }
//
//
//
//}



//    public String getJSON(String url, int timeout) {
//        HttpURLConnection c = null;
//        try {
//            URL u = new URL(url);
//            c = (HttpURLConnection) u.openConnection();
//            c.setRequestMethod("GET");
//            c.setRequestProperty("Content-length", "0");
//            c.setUseCaches(false);
//            c.setAllowUserInteraction(false);
//            c.setConnectTimeout(timeout);
//            c.setReadTimeout(timeout);
//            c.connect();
//            int status = c.getResponseCode();
//
//            switch (status) {
//                case 200:
//                case 201:
//                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line+"\n");
//                    }
//                    br.close();
//                    return sb.toString();
//            }
//
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (c != null) {
//                try {
//                    c.disconnect();
//                } catch (Exception ex) {
//                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//        return null;
//    }

    private void setting_data_load() {

        new settings_data_base(this).insertMethod("1",1,1,1,0);

    }

    private void locatiomPermissionCheck() {


        locationPermissionRequest.launch(new String[]{
                ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });



    }

    @SuppressLint({"MissingPermission", "LongLogTag"})
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            fineLocationGranted = result.getOrDefault(
                                    ACCESS_FINE_LOCATION, false);
                        }
                        Boolean coarseLocationGranted = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            coarseLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        }
                        if (fineLocationGranted != null && fineLocationGranted) {

                            // Precise location access granted.

                           Log.i("LOOOOOOCATION_PERMISSION","DONE");

                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                        } else {
                            // No location access granted.
                        }
                    }
            );



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
//    stopService(mServiceIntent);
//    Log.i("MAINACT", "onDestroy!");

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, SensorRestarterBroadcastReceiverForeground.class);
        this.sendBroadcast(broadcastIntent);
        selectedNavButton = 0;

        super.onDestroy();


    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()){
//            Toast.makeText(MainActivity.this, "KeyPRESSED", Toast.LENGTH_SHORT).show();
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()){
//            new home_screen().myOnKeyDown(keyCode);
//
//            //and so on...
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    sendVolumeBroadcast();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {

                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void  sendVolumeBroadcast(){
        Intent intent = new Intent("activity-says-hi");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        OnResume = true;
        OnPause = false;
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

    }

    @Override
    public void onBackPressed() {
        if (selectedNavButton == R.id.nav_home_button) {

            finish();
            super.onBackPressed();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.containor, new home_screen(R.id.containor)).commit();
            selectedNavButton = R.id.nav_home_button;
            homeSelected();


        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_home_button:
                vibe.playViberation(50);



                if (selectedNavButton == R.id.nav_home_button) {

                    selectedView = null;
                } else {


                    selectedView = new home_screen(R.id.containor);
                    homeSelected();
                    selectedNavButton = R.id.nav_home_button;
                }

                break;
            case R.id.nav_location_button:
                vibe.playViberation(50);

                if (selectedNavButton == R.id.nav_location_button) {

                    selectedView = null;
                } else {

                    selectedView = new MapsFragment();



                    locationSelected();

                    selectedNavButton = R.id.nav_location_button;
                }


                break;
            case R.id.nav_setting_button:
                vibe.playViberation(50);


                if (selectedNavButton == R.id.nav_setting_button) {

                    selectedView = null;
                    Toast.makeText(MainActivity.this,"1",Toast.LENGTH_SHORT).show();
                } else {



                    selectedView = new setting_screen();
                    selectedNavButton = R.id.nav_setting_button;
                    settingSelected();
                }


                break;
//            case R.id.nav_profile_button:
//            vibe.playViberation(50);
//
//
//                if (selectedNavButton == R.id.nav_profile_button) {
//
//                    selectedView = null;
//
//                } else {
//
//                    profileSelected();
//
//                    selectedView = new profile_screen();
//                    selectedNavButton = R.id.nav_profile_button;
//                }
//
//
//                break;

        }

        if (selectedView != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.containor, selectedView).commit();

        }

    }

    private void homeSelected() {

        home_nav_btn.setImageDrawable(getDrawable(R.drawable.home_selected));
        location_nav_btn.setImageDrawable(getDrawable(R.drawable.location_unselected));
        setting_nav_btn.setImageDrawable(getDrawable(R.drawable.setting_unselected));
//        profile_nav_btn.setImageDrawable(getDrawable(R.drawable.pro_file_unselected));


        home_nav_txt.setTextColor(getResources().getColor(R.color.primary_color));
        location_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        setting_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
//        pf_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));


    }

    private void locationSelected() {
        home_nav_btn.setImageDrawable(getDrawable(R.drawable.home_unselected));
        location_nav_btn.setImageDrawable(getDrawable(R.drawable.location_selected));
        setting_nav_btn.setImageDrawable(getDrawable(R.drawable.setting_unselected));
//        profile_nav_btn.setImageDrawable(getDrawable(R.drawable.pro_file_unselected));

        home_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        location_nav_txt.setTextColor(getResources().getColor(R.color.primary_color));
        setting_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
//        pf_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));

    }

    public void settingSelected() {
        home_nav_btn.setImageDrawable(getDrawable(R.drawable.home_unselected));
        location_nav_btn.setImageDrawable(getDrawable(R.drawable.location_unselected));
        setting_nav_btn.setImageDrawable(getDrawable(R.drawable.setting_selected));
//        profile_nav_btn.setImageDrawable(getDrawable(R.drawable.pro_file_unselected));

        home_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        location_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        setting_nav_txt.setTextColor(getResources().getColor(R.color.primary_color));
//        pf_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));


    }

    private void profileSelected() {

        home_nav_btn.setImageDrawable(getDrawable(R.drawable.home_unselected));
        location_nav_btn.setImageDrawable(getDrawable(R.drawable.location_unselected));
        setting_nav_btn.setImageDrawable(getDrawable(R.drawable.setting_unselected));
        profile_nav_btn.setImageDrawable(getDrawable(R.drawable.pro_file_selected));


        home_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        location_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        setting_nav_txt.setTextColor(getResources().getColor(R.color.lighter_text_color));
        pf_nav_txt.setTextColor(getResources().getColor(R.color.primary_color));

    }


}