package com.lixer.womensafetyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class settings_data_base extends SQLiteOpenHelper {

    String t_name="settings_db";
    String alert_on_vol = "alert_on_vol";
    String alert_on_power = "alert_on_power";
    String push_notification ="push_notification";
    String alert_btn_on_off = "alert_btn_on_off";
    String ids ="ids";







    public settings_data_base(Context context) {
        super(context, "women_safety", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table "+t_name+"("
                    + alert_on_vol +" INTEGER ,"
                + alert_on_power +" INTEGER ," + push_notification +" INTEGER ,"
                + ids +" TEXT ,"
                + alert_btn_on_off +" INTEGER )" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+t_name);
    }

    public Boolean insertMethod(String id, int alert_on_vol,
                                           int alert_on_power,
                                           int push_notification,
                                           int alert_btn_on_off){




        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor= db.rawQuery("Select * from "+t_name+" where "+ ids +"=?",new String[]{id});

        if (cursor.getCount()>0) {

           return false;

        }else
        {
            contentValues.put(this.ids, id);
            contentValues.put(this.alert_on_vol, alert_on_vol);
            contentValues.put(this.alert_on_power, alert_on_power);
            contentValues.put(this.push_notification, push_notification);
            contentValues.put(this.alert_btn_on_off, alert_btn_on_off);



            Long res = db.insert(t_name, null, contentValues);

            if (res == -1) {
                return false;
            } else {
                return true;
            }
        }
    }





    public Boolean updateMethod(String col_number, String id,int on_off){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor= db.rawQuery("Select * from "+t_name+" where "+ ids +"=?",new String[]{id});

        if (col_number.equals("1")){

            contentValues.put(alert_on_vol,on_off);

        }else if (col_number.equals("2")){
            contentValues.put(alert_on_power,on_off);
        }
            else if (col_number.equals("3")){
            contentValues.put(push_notification,on_off);
        }
        else if (col_number.equals("4")){
            contentValues.put(alert_btn_on_off,on_off);
        }

       if(cursor.getCount()>0){

           int res= db.update(t_name,contentValues, ids +"=?",new String[]{id});

           if (res==-1){
               return false;
           }else {
               return true;
           }

       }else {
           return false;
       }




    }







    public Cursor viewMethod( ){

        SQLiteDatabase db = this.getWritableDatabase();


        return db.rawQuery("select * from "+t_name,null);



    }












}
