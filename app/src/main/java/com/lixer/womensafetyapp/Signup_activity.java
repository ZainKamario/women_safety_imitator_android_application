package com.lixer.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Signup_activity extends AppCompatActivity implements View.OnClickListener{

    private EditText fullname;
    private EditText email;
    private EditText phone;
    private EditText cnic;
    private EditText pass;
    private EditText con_pass;

    private ImageView signup_btn;

    private TextView another_acc;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_signup);

        fullname = findViewById(R.id.name_et);
        email = findViewById(R.id.editText_email);
        phone = findViewById(R.id.phone_et);
        cnic  = findViewById(R.id.cnic_et);
        pass  = findViewById(R.id.editText_pass);
        con_pass =findViewById( R.id.con_pass_et);

        signup_btn= findViewById(R.id.signup_btn);
        another_acc = findViewById(R.id.another_acc);


        signup_btn.setOnClickListener(this);
        another_acc.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.signup_btn:

                signup();

                break;
            case R.id.another_acc:

                Intent  i  = new Intent(Signup_activity.this,login.class);
                startActivity(i);
                finish();

                break;
        }


    }

    private void createDB() {

        new user_db(this).insertMethod("1",0,0,0);
    }

    private void signup() {


        if (fullname.getText().toString().equals("")){
            fullname.setError("Please Enter Full Name First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
        }
        if (email.getText().toString().equals("")){
            email.setError("Please Enter Email First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
        }
        if (phone.getText().toString().equals("")){
            phone.setError("Please Enter Phone First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
        }
        if (cnic.getText().toString().equals("")){
            cnic.setError("Please Enter Cnic First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
        }
        if (pass.getText().toString().equals("")){
            pass.setError("Please Enter Password First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
        }
        if (con_pass.getText().toString().equals("")){
            con_pass.setError("Please Enter Confirm Password First", getResources().getDrawable(R.drawable.ic_baseline_error_24));
        }
        if(!fullname.getText().toString().equals("") && !email.getText().toString().equals("")
                && !phone.getText().toString().equals("") && !cnic.getText().toString().equals("")
                && !pass.getText().toString().equals("") && !con_pass.getText().toString().equals("")){

            if (!pass.getText().toString().equals(con_pass.getText().toString())){
                Toast.makeText(Signup_activity.this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show();
            }else{

            progressDialog= ProgressDialog.show(Signup_activity.this, "",
                    "Loading Please Wait....", true);
            httpBackgroundAsync h=new httpBackgroundAsync("https://fyp.parkh.net/api/create_user/"+fullname.getText().toString()
                    +"/"+email.getText().toString()+"/"+phone.getText().toString()+"/"+cnic.getText().toString()+"/1/"+pass.getText().toString());
            h.execute();

                Toast.makeText(Signup_activity.this, fullname.getText().toString(), Toast.LENGTH_SHORT).show();


        }
        }


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


                JSONObject jsonObject = null;
                boolean dataAdded = false;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getString("message").toString().equals("Okay.")){
                       progressDialog.dismiss();
                        Intent i = new Intent(Signup_activity.this,login.class);
                        Signup_activity.this.startActivity(i);
                        Signup_activity.this.finish();

                        pass.setText("");
                        con_pass.setText("");


                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Signup_activity.this, "Something wrong please try again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Signup_activity.this, "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }.start();


            }

        }



    }

}
