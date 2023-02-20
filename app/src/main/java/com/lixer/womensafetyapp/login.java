package com.lixer.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity {


    private EditText email;
    private EditText pass;
    private ImageView login_btn;
    private ImageView signup_btn;
    private String jsonData="";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        //  getWindow().setStatusBarColor(getResources().getColor(R.color.for_status_bar));

        Cursor cursor= new user_db(this).viewMethod();

//        if(cursor.moveToNext()){
//            if(cursor.getInt(2) == 1){
//                Intent i = new Intent(this , MainActivity.class);
//                startActivity(i);
//            }else {
//                setContentView(R.layout.activity_login);
//            }
//        }else {
            setContentView(R.layout.activity_login);
//        }






        email = findViewById(R.id.editText_email);
        pass = findViewById(R.id.editText_pass);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")){
                    email.setError("Please Enter Email First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
                }
                if (pass.getText().toString().equals("")){
                    pass.setError("Please Enter Password First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
                }
                if(!email.getText().toString().equals("") && !pass.getText().toString().equals("")){

                    progressDialog= ProgressDialog.show(login.this, "",
                            "Loading Please Wait....", true);
                    httpBackgroundAsync h=new httpBackgroundAsync("https://fyp.parkh.net/api/user_login/"+email.getText().toString()+"/"+pass.getText().toString());
                    h.execute();


                }
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,Signup_activity.class);
                startActivity(i);
                finish();
            }
        });




            createDB();






    }


    private void createDB() {

        new user_db(this).insertMethod("1",0,0,0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor cursor= new user_db(this).viewMethod();

        if(cursor.moveToNext()){
            if(cursor.getInt(2) == 1){
                Intent i = new Intent(this , MainActivity.class);
                startActivity(i);
            }else{






            }
        }




    }

    private void user_check() {

        onStart();
    }




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

       if(s != null){
            jsonData = s;

            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){
                    new user_db(login.this).updateMethod("1","1",Integer.parseInt((String) jsonObject.get("institution_id")));
                    new user_db(login.this).updateMethod("3", "1", Integer.parseInt((String) jsonObject.get("user_id")));
                    new user_db(login.this).updateMethod("2","1",1);
                    progressDialog.dismiss();
                    login.this.onStart();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "email or password wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }else{

           new CountDownTimer(1000,1){

               @Override
               public void onTick(long millisUntilFinished) {

               }

               @Override
               public void onFinish() {
                   progressDialog.dismiss();
                   Toast.makeText(login.this, "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
               }
           }.start();


            }

        }



    }




}