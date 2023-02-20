package com.lixer.womensafetyapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

/**
 * Created by fabio on 30/01/2016.
 */


public class SensorServiceForeground extends Service {
    public int counter = 0;
    private int i = 0;
    private IntentFilter filter;
    private Handler handler;
    private Runnable r;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest request;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();

        else
            startForeground(1, new Notification());



        request = LocationRequest.create();
        request.setInterval(5000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());



    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public FusedLocationProviderClient getFusedLocationClient() {
        return fusedLocationClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer(this);
        locationUpdate(this);


        return START_STICKY;
    }

    private LocationCallback locationCallback =new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLastLocation();
            if (location != null) {

                Log.i("NEW_LOOOOOCATION", String.valueOf(location.getLatitude())
                        + " , " + String.valueOf(location.getLongitude()));

                Cursor cursor = new user_db(SensorServiceForeground.this).viewMethod();
                cursor.moveToNext();
                String user_id = String.valueOf(cursor.getInt(1));

                httpBackgroundAsync h2= new httpBackgroundAsync("https://fyp.parkh.net/api/user_location_update/"+user_id+"/"
                        +String.valueOf(location.getLatitude())+"/"+String.valueOf(location.getLongitude()));
                h2.execute();



//                    Toast.makeText(ctx, String.valueOf(location.getLatitude())
//                            + " , " + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            locationUpdate(ctx);
//                        }
//                    },500);

            } else{
                Log.i("NEW_LOOOOOCATION", "null");
            }
        }
    };

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




            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){



                }else{

                    Log.i("SensorServiceForeground", "Something went wrong");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }



    }

    @SuppressLint("MissingPermission")
    private void locationUpdate(Context ctx) {

//        if (ActivityCompat.checkSelfPermission(ctx,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(ctx,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }


        fusedLocationClient.requestLocationUpdates(request,locationCallback,new Handler().getLooper());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, SensorRestarterBroadcastReceiverForeground.class);
        this.sendBroadcast(broadcastIntent);
    }


    private Timer timer;
    private TimerTask timerTask;

    public void startTimer(Context ctx) {


        filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        registerReceiver(mMessageReceiver, filter);


        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {







                                Log.i("Count", "=========  " + (counter++));

            }
        };

        locationUpdate(this);
        timer.schedule(timerTask, 1000, 1000); //

    }

    private boolean wasScreenOn=true;
    private boolean alerted = false;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent


            i++;
            handler = new Handler();
            r = new Runnable() {

                @Override
                public void run() {
                    i = 0;
                }
            };

            if (i == 1) {
                //Single click
                handler.postDelayed(r, 350);
            } else if (i == 2 && !alerted) {
                //Double click
                i = 0;
                Log.e("LOB","onReceive");
                Cursor cursor = new user_db(SensorServiceForeground.this).viewMethod();
                cursor.moveToNext();
                String user_id = String.valueOf(cursor.getInt(1));
                HttpBackgroundAsync2 h2= new HttpBackgroundAsync2("https://fyp.parkh.net/api/alert_post/"+user_id);
                h2.execute();
                new Viberation(SensorServiceForeground.this).playViberation(1000);
                alerted =true;
            }else if (i == 2 && alerted) {
                //Double click
                i = 0;
                Log.e("LOB","onReceive");
                Cursor cursor = new user_db(SensorServiceForeground.this).viewMethod();
                cursor.moveToNext();
                String user_id = String.valueOf(cursor.getInt(1));
                new Viberation(SensorServiceForeground.this).playViberation(1000);
                HttpBackgroundAsync2 h2= new HttpBackgroundAsync2("https://fyp.parkh.net/api/disable_alert_post/"+user_id);
                h2.execute();
                alerted =false;
            }
//
//            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//                // do whatever you need to do here
//
//                i++;
//                handler = new Handler();
//                r = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        i = 0;
//                    }
//                };
//
//                if (i == 1) {
//                    //Single click
//                    handler.postDelayed(r, 250);
//                } else if (i == 2) {
//                    //Double click
//                    i = 0;
//                    Log.e("LOB","wasScreenOn"+wasScreenOn);
//                }
//
//
//                wasScreenOn = false;
//
//            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//                // and do whatever you need to do here
//                wasScreenOn = true;
//
//                i++;
//                handler = new Handler();
//                r = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        i = 0;
//                    }
//                };
//
//                if (i == 1) {
//                    //Single click
//                    handler.postDelayed(r, 250);
//                } else if (i == 2) {
//                    //Double click
//                    i = 0;
//                    Toast.makeText(SensorServiceForeground.this, "KeyZ", Toast.LENGTH_SHORT).show();
//                }
//
//            } else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
//                Log.e("LOB","userpresent");
//                Log.e("LOB","wasScreenOn"+wasScreenOn);
//
//                i++;
//                Handler handler = new Handler();
//                r = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        i = 0;
//                    }
//                };
//
//                if (i == 1) {
//                    //Single click
//                    handler.postDelayed(r, 250);
//                } else if (i == 2) {
//                    //Double click
//                    i = 0;
//                    Toast.makeText(SensorServiceForeground.this, "KeyZ", Toast.LENGTH_SHORT).show();
//                }
//
//            }


        }
    };

    public class HttpBackgroundAsync2 extends AsyncTask<Void,Void,String> {

        String url;
        String json_string;



        public HttpBackgroundAsync2(String url) {
            this.url = url;

        }

        public HttpBackgroundAsync2() {

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




            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){



                }else{

                    Log.i("SensorServiceForeground", "Something went wrong");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }



    }

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
            Toast.makeText(SensorServiceForeground.this, "KeyZ", Toast.LENGTH_SHORT).show();
        }


    }


    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}