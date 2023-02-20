package com.lixer.womensafetyapp;

import android.content.Context;
import android.os.Vibrator;

public class Viberation {

    private Vibrator vibe;

    private Context cxt;

    public Viberation(Context cxt) {
        this.cxt = cxt;
        vibe = (Vibrator) cxt.getSystemService(Context.VIBRATOR_SERVICE);
    }



    public void playViberation(int mileSeconds){


     vibe.vibrate(mileSeconds);

    }
    public void stopViberation(){


        
        vibe.cancel();

    }

}
