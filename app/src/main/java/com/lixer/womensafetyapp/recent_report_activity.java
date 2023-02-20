package com.lixer.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
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

public class recent_report_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Recent_Report_Adapter adapter;

    private ImageView back_btn;
    private ProgressDialog progressDialog;
    String user_id = "";
    String institute_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setContentView(R.layout.activity_recent_report);

        recyclerView = findViewById(R.id.message_box_recycler);
        back_btn = findViewById(R.id.recent_report_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog = ProgressDialog.show(this, "",
                "Loading Please Wait....", true);

        Cursor user_data = new user_db(this).viewMethod();


        if(user_data.moveToNext()) {

             user_id = String.valueOf(user_data.getInt(1));
            institute_id = String.valueOf(user_data.getInt(0));

            httpBackgroundAsync h = new httpBackgroundAsync("https://fyp.parkh.net/api/recent_reports/"+user_id+"/"+institute_id);
            h.execute();

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


            if (s != null){

            JSONObject jsonObject = null;
            boolean dataAdded = false;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").toString().equals("Okay.")){
                    progressDialog.dismiss();
                    JSONArray reportArray= jsonObject.getJSONArray("reports");
                    JSONArray msgsArray= jsonObject.getJSONArray("msgs");
                    adapter = new Recent_Report_Adapter(reportArray,msgsArray);
                    LinearLayoutManager ln= new LinearLayoutManager(recent_report_activity.this);
                    ln.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(ln);
                    recyclerView.setAdapter(adapter);



                }else{
                    progressDialog.dismiss();
                    Toast.makeText(recent_report_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }else{


                        progressDialog.dismiss();
                        Toast.makeText(recent_report_activity.this, "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
                        recent_report_activity.this.finish();
            }}



    }


}