package com.lixer.womensafetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Message_box_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JSONArray msgsArray;

    public Message_box_RecyclerViewAdapter(JSONArray msgsArray) {
        this.msgsArray = msgsArray;
    }

    @Override
    public int getItemViewType(int position) {

        try {
            JSONObject jsonObject = msgsArray.getJSONObject(position);

            if(Integer.parseInt(jsonObject.getString("sender")) == 0){

               return 1;
            }else if(Integer.parseInt(jsonObject.getString("sender")) == 1){

                return 2;
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 3;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



            if(viewType == 1){

                return new viewholderRight(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box_design_right,parent,false));

            }else if(viewType == 2){

                return new viewholderLeft(  LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box_design_left,parent,false));
            }

            




        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            JSONObject jsonObject = msgsArray.getJSONObject(position);

            if(!jsonObject.getString("sender").equals("")){

            if(Integer.parseInt(jsonObject.getString("sender")) == 0){

                    ((viewholderRight) holder).setData(position);

            }else if(Integer.parseInt(jsonObject.getString("sender")) == 1){
                ((viewholderLeft) holder).setData(position);
            }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        

    }

    @Override
    public int getItemCount() {
        return msgsArray.length();
    }

    public class viewholderLeft extends RecyclerView.ViewHolder {

        private TextView report_message_data;

        private TextView report_title;

        public viewholderLeft(@NonNull View itemView) {
            super(itemView);

            report_title = itemView.findViewById(R.id.report_title);
            report_message_data = itemView.findViewById(R.id.report_message_data);

            

        }

        public void setData(int position) {

            try {

                JSONObject jsonObject = msgsArray.getJSONObject(position);
                report_title.setText(jsonObject.getString("msg_subject"));
                report_message_data.setText(jsonObject.getString("msg_content"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public class viewholderRight extends RecyclerView.ViewHolder {


        private TextView report_title;
        private TextView report_message_data;
        public viewholderRight(@NonNull View itemView) {
            super(itemView);

            report_title = itemView.findViewById(R.id.report_title);
            report_message_data = itemView.findViewById(R.id.report_message_data);



        }

        public void setData(int position) {

            try {

                JSONObject jsonObject = msgsArray.getJSONObject(position);
                report_title.setText(jsonObject.getString("msg_subject"));
                report_message_data.setText(jsonObject.getString("msg_content"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
