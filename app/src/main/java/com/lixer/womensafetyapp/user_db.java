package com.lixer.womensafetyapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class user_db extends SQLiteOpenHelper {

    String t_name="user_db";
    String institution_id = "institution_id";
    String user_id = "user_id";
    String user_login_or_not = "user_login_or_not";
    String ids ="ids";


    public user_db(Context context) {
        super(context, "user_db", null, 1);

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table "+t_name+"("
                + institution_id +" INTEGER ,"
                + user_id +" INTEGER ," + user_login_or_not +" INTEGER ,"
                + ids +" TEXT )"
               );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+t_name);
    }

    public Boolean insertMethod(String id, int institution_id,
                                int user_login_or_not,
                                int user_id){




        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor= db.rawQuery("Select * from "+t_name+" where "+ ids +"=?",new String[]{id});

        if (cursor.getCount()>0) {

            return false;

        }else
        {
            contentValues.put(this.ids, id);
            contentValues.put(this.institution_id, institution_id);
            contentValues.put(this.user_id, user_id);
            contentValues.put(this.user_login_or_not, user_login_or_not);




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

            contentValues.put(institution_id,on_off);

        }else if (col_number.equals("2")){
            contentValues.put(user_login_or_not,on_off);
        }
        else if (col_number.equals("3")){
            contentValues.put(user_id,on_off);
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




