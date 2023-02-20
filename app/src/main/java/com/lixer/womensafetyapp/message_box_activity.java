package com.lixer.womensafetyapp;

import static com.lixer.womensafetyapp.create_report_activity.report_model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class message_box_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Message_box_RecyclerViewAdapter adapter;
    private EditText message_edit_text ;
    private ConstraintLayout message_bottom_box;

    private ImageView back_btn;

    private ProgressDialog progressDialog;

    private SoftInputAssist softInputAssist;

    public static JSONObject jsonObjectMSG;

    private ImageView send_message_btn;
    LinearLayoutManager ln;



    public static int x=0;

    String date;

    String report_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }


        setContentView(R.layout.activity_message_box);
        softInputAssist = new SoftInputAssist(this);




        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        recyclerView = findViewById(R.id.message_box_recycler);
        send_message_btn  = findViewById(R.id.send_message_btn);

        back_btn = findViewById(R.id.recent_report_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        message_edit_text  = findViewById(R.id.message_box_edit_text);
        message_bottom_box = findViewById(R.id.message_bottom_box);

//        message_edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                if(hasFocus){
//
//                    if (im != null){
//                        im.showSoftInput(message_bottom_box,InputMethodManager.SHOW_FORCED);
//                    }else{
//                        im.hideSoftInputFromWindow(message_bottom_box.getWindowToken(),0);
//                    }
//
//                }else{
//                    if(im == null){
//                        im.hideSoftInputFromWindow(message_bottom_box.getWindowToken(),0);
//                    }
//                }
//            }
//        });






        if(report_model!=null) {
            Toast.makeText(this, "Title : " + report_model.getReport_title() + "\nData : " + report_model.getReport_data(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog = ProgressDialog.show(this, "",
                "Loading Please Wait....", true);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        httpBackgroundAsync h = null;
        try {
            report_id = jsonObjectMSG.getString("report_id");
            h = new httpBackgroundAsync("https://fyp.parkh.net/api/msgs_fetch/"+report_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        h.execute();


        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!message_edit_text.getText().toString().equals("")){
                    Cursor user = new user_db(message_box_activity.this).viewMethod();
                    user.moveToNext();
                  HttpBackgroundAsync2 h2= new HttpBackgroundAsync2("https://fyp.parkh.net/api/send_msg/"+report_id+"/"
                            +message_edit_text.getText().toString()+"/"+String.valueOf(user.getInt(1))+"/"
                          +String.valueOf(user.getInt(0))+"/0/0/"+date);
                  h2.execute();

                }

            }
        });





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

                    JSONArray msgsArray= jsonObject.getJSONArray("msgs");
                    adapter = new Message_box_RecyclerViewAdapter(msgsArray);
                    ln= new LinearLayoutManager(message_box_activity.this);
                    ln.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(ln);
                    ln.setStackFromEnd(true);
                    recyclerView.setAdapter(adapter);


                }else{
                    progressDialog.dismiss();
                    Toast.makeText(message_box_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }else {

                new CountDownTimer(1000,1){

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        Toast.makeText(message_box_activity.this, "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }.start();

            }}



    }


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



           if (s != null){

            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){


                    JSONObject jsonObject1 = new JSONObject();
                    Cursor cursor = new user_db(message_box_activity.this).viewMethod();

                    cursor.moveToNext();

                    jsonObject1.put("report_id",report_id);
                    jsonObject1.put("msg_subject","");
                    jsonObject1.put("msg_content",message_edit_text.getText().toString());
                    jsonObject1.put("from_id",String.valueOf(cursor.getInt(1)));
                    jsonObject1.put("to_id",String.valueOf(cursor.getInt(0)));
                    jsonObject1.put("report_or_not",0);
                    jsonObject1.put("sender",0);
                    jsonObject1.put("msg_date",date);

                    adapter.msgsArray.put(jsonObject1);
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                    ln.setStackFromEnd(true);
                    adapter.notifyDataSetChanged();
                    message_edit_text.setText("");







                }else{

                    Toast.makeText(message_box_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }else{
               Toast.makeText(message_box_activity.this, "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
           }}



    }



    @Override
    protected void onResume() {
        softInputAssist.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        softInputAssist.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        softInputAssist.onDestroy();
        super.onDestroy();
    }
}