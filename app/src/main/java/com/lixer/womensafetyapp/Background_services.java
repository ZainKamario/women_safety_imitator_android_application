package com.lixer.womensafetyapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media.VolumeProviderCompat;


public class Background_services extends Service {

    int i=0;

    MediaSessionCompat mediaSession;



    @Override
    public void onCreate() {
        super.onCreate();

//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//                new IntentFilter("activity-says-hi"));



//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));


        mediaSession = new MediaSessionCompat(this, "Background_services");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                .build());

        //this will only work on Lollipop and up, see https://code.google.com/p/android/issues/detail?id=224134
        VolumeProviderCompat myVolumeProvider =
                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/50) {
                    @Override
                    public void onAdjustVolume(int direction) {
                /*
                -1 -- volume down
                1 -- volume up
                0 -- volume button released
                 */

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
//            alert_btn_unactive.setClickable(false);
//            alert_btn_unactive.animate().alpha(0).setDuration(300).start();
//            alert_btn_active.animate().alpha(1).setDuration(300).start();
//            alert_btn_active.setVisibility(View.VISIBLE);
//            alert_btn_active.setClickable(true);
//                            Toast.makeText(context, "KeyPressed", Toast.LENGTH_SHORT).show();

                        }


                    }
                };

        mediaSession.setPlaybackToRemote(myVolumeProvider);
        mediaSession.setActive(true);




//        doSomethingCauseVolumeKeyPressed(this);

    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

//            doSomethingCauseVolumeKeyPressed(context);

            Toast.makeText(context,"keyPre",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }



    private void doSomethingCauseVolumeKeyPressed(Context context) {

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
//            alert_btn_unactive.setClickable(false);
//            alert_btn_unactive.animate().alpha(0).setDuration(300).start();
//            alert_btn_active.animate().alpha(1).setDuration(300).start();
//            alert_btn_active.setVisibility(View.VISIBLE);
//            alert_btn_active.setClickable(true);
            Toast.makeText(context, "KeyPressed", Toast.LENGTH_SHORT).show();

        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(Background_services.this, "Service is running", Toast.LENGTH_SHORT).show();
//            }
//        },15000);

    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}