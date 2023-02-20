package com.lixer.womensafetyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class create_report_page_2 extends Fragment implements View.OnClickListener {

    private View root_view;

    private ConstraintLayout option_1;

    private ConstraintLayout submit_btn;

    private ImageView option_img_1;

    private boolean option1_active;

    private EditText report_data_editText;

    private ProgressDialog progressDialog;

    private create_new_report_model report_model;

    public create_report_page_2(create_new_report_model report_model) {
        this.report_model = report_model;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.create_report_page_2, container, false);

        option_1  = root_view.findViewById(R.id.option_1);

        submit_btn  = root_view.findViewById(R.id.report_submit_btn);

        option_img_1  = root_view.findViewById(R.id.option_1_img);

        report_data_editText = root_view.findViewById(R.id.report_data_edit_text);

        option1_active = true;

        option_1.setOnClickListener(this);
        submit_btn.setOnClickListener(this);



        return root_view;

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option_1:

                if(option1_active){
                    option1_active = false;
                    option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));

                }else {
                    option1_active = true;
                    option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));

                }


                break;
            case R.id.report_submit_btn:

                if(option1_active){

                    if(!report_data_editText.getText().toString().equals("")){
                        report_model.setReport_data(report_data_editText.getText().toString());

//                       Intent intent = new Intent(root_view.getContext(),message_box_activity.class);
//                        root_view.getContext().startActivity(intent);

                        Cursor user_data = new user_db(root_view.getContext()).viewMethod();


                        if(user_data.moveToNext()) {

                            String user_id = String.valueOf(user_data.getInt(1));
                            String institute_id = String.valueOf(user_data.getInt(0));

                            progressDialog = ProgressDialog.show(root_view.getContext(), "",
                                    "Loading Please Wait....", true);
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                            httpBackgroundAsync h = new httpBackgroundAsync("https://fyp.parkh.net/api/create_report/"+report_model.getReport_title()
                                    +"/"+report_model.getReport_data()+"/"+user_id+"/"+institute_id+"/1/0/"+ date);
                            h.execute();

                        }

                    }else {
                        Toast.makeText(root_view.getContext(), " Please Input Report Data  ", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(root_view.getContext(), " Please Accept Our Term And Conditions  ", Toast.LENGTH_SHORT).show();

                }

                break;

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
                    getActivity().finish();


                }else{
                    progressDialog.dismiss();
                    Toast.makeText(root_view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(root_view.getContext(), "Something went worng please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }.start();
            }}



    }



}