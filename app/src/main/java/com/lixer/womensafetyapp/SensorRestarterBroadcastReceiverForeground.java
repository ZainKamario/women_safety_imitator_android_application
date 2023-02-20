package com.lixer.womensafetyapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by fabio on 24/01/2016.
 */
public class SensorRestarterBroadcastReceiverForeground extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, SensorServiceForeground.class));
        } else {
            context.startService(new Intent(context, SensorServiceForeground.class));
        }
    }

}
