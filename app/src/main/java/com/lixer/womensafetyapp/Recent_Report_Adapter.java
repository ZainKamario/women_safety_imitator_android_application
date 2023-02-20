package com.lixer.womensafetyapp;

import static com.lixer.womensafetyapp.message_box_activity.jsonObjectMSG;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Recent_Report_Adapter extends RecyclerView.Adapter<Recent_Report_Adapter.viewholder> {


    JSONArray reportArray;
    JSONArray msgsArray;
    public Recent_Report_Adapter(JSONArray reportArray, JSONArray msgsArray) {
        this.reportArray= reportArray;
        this.msgsArray = msgsArray;
    }

    @NonNull
    @Override
    public Recent_Report_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_report_card_design,parent,false);
            message_box_activity.x=0;

        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Recent_Report_Adapter.viewholder holder, int position) {

        try {
            holder.setData(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return reportArray.length();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);



        }

        public void setData(int pos) throws JSONException {
            JSONObject jsonObject = msgsArray.getJSONObject(pos);
            String msgTitle ="";
            if(jsonObject.getString("msg_subject").length()>=18){
                msgTitle = jsonObject.getString("msg_subject")+"...";
            }else{
                msgTitle = jsonObject.getString("msg_subject");
            }
            title.setText(msgTitle);
            content.setText(jsonObject.getString("msg_content"));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), message_box_activity.class);

                    jsonObjectMSG  = jsonObject;
                    itemView.getContext().startActivity(i);
                }
            });

        }
    }
}
